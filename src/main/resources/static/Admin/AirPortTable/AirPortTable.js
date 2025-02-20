let pageSize = 10;
let CurrentPage = 0;
let totalPages = 10;
let SearchQuery="";
function updatePaginationControls(totalCount) {
    totalPages = Math.ceil(totalCount / pageSize);
    $('#prevPage').prop('disabled', CurrentPage === 1); // Sửa điều kiện
    $('#nextPage').prop('disabled', CurrentPage === totalPages);

    let pageNumbersHtml = '';
    for (let i = 1; i <= totalPages; i++) {
        pageNumbersHtml += `<li class="page-item ${i === CurrentPage ? 'active' : ''}">
            <span class="page-link" onclick="changePage(${i})">${i}</span>
        </li>`;
    }
    $('#pageNumbers').html(pageNumbersHtml);
}

$('#prevPage').click(function () {
    if (CurrentPage > 1) {
        CurrentPage--;
        fetchAirPort(CurrentPage, pageSize,SearchQuery);
    }
})
$('#nextPage').click(function () {
    if (CurrentPage < totalPages) {
        CurrentPage++;
        fetchAirPort(CurrentPage, pageSize,SearchQuery);
    }
})

function changePage(page) {
    if (page >= 1 && page <= totalPages) {
        CurrentPage = page;
        fetchAirPort(CurrentPage - 1, pageSize,SearchQuery); // Điều chỉnh chỉ mục trang (bắt đầu từ 0)
    }
}

function fetchAirPort(page, size,query) {
    const token = document.getElementById('token') ? document.getElementById('token').textContent : null;
    const id = document.getElementById('IdCountry') ? document.getElementById('IdCountry').textContent : null;

    if (!token) {
        console.error('No access token found.');
        return; // Exit early if no token is available
    }

    const url = `http://localhost:8686/AirPort/${id}?page=${page}&size=${size}&name=${query}`;
    $.ajax({
        url: url,
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${token}`
        },
        success: function (response) {
            const AirPort = response.content;
            const totalCount = response.totalElements; // Đổi sang `totalElements` nếu API trả tổng số bản ghi

            updatePaginationControls(totalCount);
            $('#AirPortTableBody').empty();
            AirPort.forEach((airport, index) => {
                $('#AirPortTableBody').append(`
                    <tr>
                        <td>${index + 1 + page * size}</td> <!-- Điều chỉnh chỉ số theo trang -->
                        <td>${airport.name}</td>
                        <td>${airport.city.name}</td>
                          <td>
                    <a  href="/Admin/AirPort/Edit/${airport.id}" style="background-color: #4299e1;border-color: #4299e1" class="btn btn-info"><i style="color: white" class="fa fa-pencil"></i></a>
                   
                    </td>
                    </tr>
                `);
            });
        },
        error: function (xhr, status, error) {
            console.error('Error fetching data:', error);
        }
    });
}
function SearchAirport(){
    SearchQuery=document.getElementById("searchAirPort").value;
    CurrentPage=0;
    fetchAirPort(CurrentPage,pageSize,SearchQuery)
}
// Gọi lần đầu
fetchAirPort(CurrentPage, pageSize,SearchQuery);
