const pageSize = 10;
let CurrentPage = 0;
let totalPages = 10;
let SearchQuery="";
function updatePaginationControls(totalCount) {
    totalPages = Math.ceil(totalCount / pageSize);
    $('#prevPage').prop('disabled', CurrentPage);
    $('#nextPage').prop('disabled', CurrentPage === totalPages);
    let pageNumbersHtml = '';
    for (let i = 1; i <= totalPages; i++) {
        pageNumbersHtml += `<li class="page-item ${i === CurrentPage ? 'active' : ''}">
<span class="page-link" onclick="changePage(${i})">${i}</span>
</li>`
    }
    $('#pageNumbers').html(pageNumbersHtml);
}
function changePage(page){
    if (page >= 1 && page <= totalPages) {
        CurrentPage = page;
        fetchCity(CurrentPage, pageSize,SearchQuery);  // Fetch countries for the selected page
    }
}
function fetchCity(page,size,query){
    const token = document.getElementById('token') ? document.getElementById('token').textContent : null;
    const id=document.getElementById('idCountry') ? document.getElementById('idCountry').textContent : null;
    console.log(id)
    if (!token) {
        console.error('No access token found.');
        return; // Exit early if no token is available
    }
    const url = `http://localhost:8686/City/FindCityPage/${id}?page=${page}&size=${size}&name=${query}`;
    $.ajax({
        url: url,
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        },
        success: function (response) {

            const countries = response.content;
            const totalCount = response.totalPages;

            updatePaginationControls(totalCount)
            $('#CityTableBody').empty();
            countries.forEach((country, index) => {
                $('#CityTableBody').append(`<tr>
                    <td>${index + 1}</td>
                    <td>${country.name}</td>
                  <td>
                    <a href="/Admin/City/${country.id}" style="background-color: #4299e1;border-color: #4299e1" class="btn btn-info"><i style="color: white" class="fa fa-pencil"></i></a>
                    <a href="/Admin/DeleteCity/${country.id}" class="btn btn-danger">
                    <i style="color: white" class="fa fa-trash"></i>
</a>
                    </td>
</tr>`);
            });
            console.log(countries)
            // You can further process and display the countries as needed

        },
        error: function (xhr, status, error) {
            console.error('Error fetching data:', error);
            // Optionally, handle specific error responses or show a message to the user
        }
    });
}
$('#prevPage').click(function (){
    if(CurrentPage>1){
        CurrentPage--;
        fetchCity(CurrentPage,pageSize,SearchQuery);
    }
})
$('#nextPage').click(function (){
    if(CurrentPage<totalPages){
        CurrentPage++;
        fetchCity(CurrentPage,pageSize,SearchQuery);
    }
})
function SearchCities(){
    SearchQuery=document.getElementById("searchCity").value;
    CurrentPage=0;
    fetchCity(CurrentPage,pageSize,SearchQuery)
}
fetchCity(CurrentPage,pageSize,SearchQuery);