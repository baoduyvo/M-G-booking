document.addEventListener("DOMContentLoaded", function () {
    var departureTimeInput = document.getElementById("datetimepicker");
    var arrivalTimeInput = document.getElementById("Departure_Time");

    var arrivalDateValue = arrivalTimeInput.value || new Date().toISOString();// Nếu không có giá trị, sử dụng ngày hiện tại
    var arrivalDate = new Date(arrivalDateValue);
    arrivalDate.setHours(arrivalDate.getHours() + 2); // Thêm 2 giờ
    arrivalDateValue = arrivalDate.toISOString();


    // Khởi tạo flatpickr cho "datetimepicker"
    var datetimepicker = flatpickr("#datetimepicker", {
        enableTime: true,
        dateFormat: "Y-m-d\\TH:i", // Định dạng ngày giờ ISO
        altInput: true,            // Hiển thị định dạng thân thiện hơn
        altFormat: "F j, Y, H:i",  // Định dạng thân thiện hơn
        time_24hr: true,           // Định dạng 24 giờ
        timezone: "Asia/Ho_Chi_Minh",
        minDate: arrivalDateValue, // Ngày tối thiểu là ngày khởi hành
    });

    // Cập nhật minDate của datetimepicker khi người dùng thay đổi Departure_Time
    flatpickr("#Departure_Time", {
        enableTime: true,
        dateFormat: "Y-m-d\\TH:i",  // Định dạng ngày giờ ISO
        altInput: true,             // Hiển thị định dạng thân thiện hơn
        altFormat: "F j, Y, H:i",   // Định dạng thân thiện hơn
        time_24hr: true,            // Định dạng 24 giờ
        defaultDate: arrivalDateValue,  // Mặc định là ngày khởi hành
        timezone: "Asia/Ho_Chi_Minh",
        minDate: "today",          // Ngày tối thiểu là ngày hiện tại
        onChange: function(selectedDates) {
            // Lấy ngày khởi hành đã chọn
            var selectedDate = selectedDates[0];

            // Thêm 1 ngày vào ngày khởi hành để làm ngày tối thiểu cho datetimepicker
            var minReturnDate = new Date(selectedDate);
            minReturnDate.setDate(minReturnDate.getDate() + 1); // Thêm 1 ngày

            // Cập nhật minDate của datetimepicker
            datetimepicker.set("minDate", minReturnDate); // Đặt minDate của datetimepicker từ ngày đã chọn
        }
    });
});


console.log(document.getElementById("Departure_Time").value)
let detailIndex = 0; // Tracks the number of detail flights
const detailFlights = []; // Array to store detail flights locally
const detailFlightsContainer = document.getElementById("detailFlights");
const addDetailFlightButton = document.getElementById("addDetailFlight");

// Function to render a detail flight input template
function renderDetailFlight(index, type = "", price = 0, quantity = 0) {
    const detailFlightTemplate = `
        <div class="detail-flight mb-3" id="detailFlight-${index}">
            <div class="row">
                <div class="col-md-4">
                    <label for="detailFlights[${index}].type" class="form-label">Type</label>
                    <select class="form-control" name="detailFlights[${index}].type" required>
                        <option value="Popular" ${type === "Popular" ? "selected" : ""}>Popular</option>
                        <option value="Premium Economy" ${type === "Premium Economy" ? "selected" : ""}>Premium Economy</option>
                        <option value="Merchant" ${type === "Merchant" ? "selected" : ""}>Merchant</option>
                        <option value="First Class" ${type === "First Class" ? "selected" : ""}>First Class</option>
                    </select>
                </div>
                <div class="col-md-4">
                    <label for="detailFlights[${index}].price" class="form-label">Price</label>
                    <input type="number" step="0.01" class="form-control" name="detailFlights[${index}].price" value="${price}" placeholder="Enter Price" required>
                </div>
                <div class="col-md-4">
                    <label for="detailFlights[${index}].quantity" class="form-label">Quantity</label>
                    <input type="number" class="form-control" name="detailFlights[${index}].quantity" value="${quantity}" placeholder="Enter Quantity" required>
                </div>
            </div>
            <div class="text-end mt-2">
                <button type="button" class="btn btn-danger btn-sm removeDetailFlight" data-index="${index}">Remove</button>
            </div>
        </div>
    `;
    detailFlightsContainer.insertAdjacentHTML("beforeend", detailFlightTemplate);

    // Add a blank entry to the detailFlights array
    detailFlights.push({ index, type, price, quantity });

    // Listen for changes in inputs
    const detailFlightElement = document.getElementById(`detailFlight-${index}`);
    detailFlightElement.querySelectorAll("input").forEach(input => {
        input.addEventListener("input", () => {
            const field = input.name.split(".")[1]; // Extract the field name (type, price, quantity)
            const flightDetail = detailFlights.find(df => df.index === index);
            if (flightDetail) {
                flightDetail[field] = input.value;
            }
        });
    });

    // Add event listener to the Remove button
    document.querySelector(`#detailFlight-${index} .removeDetailFlight`).addEventListener("click", (e) => {
        const detailFlightId = e.target.getAttribute("data-index");
        document.getElementById(`detailFlight-${detailFlightId}`).remove();

        // Remove from detailFlights array
        const indexToRemove = detailFlights.findIndex(df => df.index == detailFlightId);
        if (indexToRemove > -1) {
            detailFlights.splice(indexToRemove, 1);
        }
    });
}

// Add the first (default) detail flight entry on page load
renderDetailFlight(detailIndex, "", 0, 0);  // Default values
detailIndex++;  // Increment after initial entry

// Event listener for adding additional flights
addDetailFlightButton.addEventListener("click", () => {
    renderDetailFlight(detailIndex);
    detailIndex++;
});

let pageSize = 10;
let CurrentPage = 0;
let TotalPages = 10;
let SearchQuery="";
function updatePaginationControls(totalCount) {
    totalPages = Math.ceil(totalCount / pageSize);
    $('#prevPage').prop('disabled', CurrentPage === 1);
    $('#nextPage').prop('disabled', CurrentPage === totalPages);
    let pageNumbersHtml = '';
    for (let i = 0; i < totalPages; i++) {
        pageNumbersHtml += `<li class="page-item ${i === CurrentPage ? 'active' : ''}">
            <span class="page-link" onclick="changePage(${i})">${i+1}</span>
        </li>`;
    }
    $('#pageNumbers').html(pageNumbersHtml);
}

$('#prevPageItem').click(function () {
    if (CurrentPage >= 0) {
        CurrentPage--;

        fetchFlight(CurrentPage, pageSize,SearchQuery);
    }

    // Disable the button if CurrentPage is 0 or less
    if (CurrentPage <= 0) {
        $('#prevPageItem').prop('disabled', true); // Disable the button
    } else {
        $('#prevPageItem').prop('disabled', false); // Enable the button
    }
});
$('#nextPage').click(function () {
    if (CurrentPage < TotalPages) {
        CurrentPage++;

        fetchFlight(CurrentPage,pageSize,SearchQuery);
    }
})

function changePage(page) {

    if (page >= 0 && page <= totalPages) {
        CurrentPage = page;
        fetchFlight(CurrentPage , pageSize,SearchQuery);
    }
}


function fetchFlight(page, size,query) {
    const token = document.getElementById('token') ? document.getElementById('token').textContent : null;
    const id = document.getElementById('IdCountry') ? document.getElementById('IdCountry').textContent : null;
    if (!token) {
        console.error('No access token found.');
        return;
    }
    const url = `http://localhost:8686/Flight/FindAll/${id}?page=${page}&size=${size}&name=${query}`;
    $.ajax({
        url: url, method: 'GET', headers: {
            'Authorization': `Bearer ${token}`
        }, success: function (response) {
            const flight = response.content;
            const totalCount = response.totalElements;
            updatePaginationControls(totalCount);
            $('#FlightTableBody').empty();
            flight.forEach((flights, index) => {
                $('#FlightTableBody').append(`<tr>
                <td>${index + 1 + page * size}</td>
                <td>${flights.nameAirline}</td>
                <td>${flights.departure_airport}</td>
                <td>${flights.arrival_airport}</td>
                 <td>
                    <a href="/Admin/Flight/Edit/${flights.id}"  style="background-color: #4299e1;border-color: #4299e1" class="btn btn-info"><i style="color: white" class="fa fa-pencil"></i></a>
             
                    </td>
            </tr>`)
            })
        },
        error: function (xhr, status, error) {
            console.error('Error fetching data:', error);
        }
    })

}
function SearchFlight(){
    SearchQuery=document.getElementById("searchFlight").value;
    CurrentPage=0;
    fetchFlight(CurrentPage,pageSize,SearchQuery)
}
fetchFlight(CurrentPage,pageSize,SearchQuery);