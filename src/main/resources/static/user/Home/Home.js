document.addEventListener('DOMContentLoaded',function (){
    const TripTypeValue=document.getElementById('TripTypeValue').value;
    if(TripTypeValue==='true'){
        document.getElementById('ArrivalDate').style.display='block';
    }else{
        const arrivalTimeInput = document.getElementById('arrivalTime');
        if (arrivalTimeInput) {
            arrivalTimeInput.value = ''; // Xóa giá trị đã chọn
            if (arrivalTimeInput._flatpickr) {
                arrivalTimeInput._flatpickr.clear(); // Xóa Flatpickr nếu đã được khởi tạo
            }
        }
        document.getElementById('ArrivalDate').style.display='none';

    }
    const flexSwitchCheckDefault=document.getElementById('flexSwitchCheckDefault');
    console.log(flexSwitchCheckDefault.value)
    if(flexSwitchCheckDefault.checked){

        document.getElementById('RoomNumber').style.display='block';
        document.getElementById('TimeHotel').style.display='block';
        document.getElementById('AtHotel').style.display='block'
    }else{
        console.log('test')
        document.getElementById('RoomNumber').style.display='none';
        document.getElementById('TimeHotel').style.display='none';
        document.getElementById('AtHotel').style.display='none'
    }
})
document.getElementById("from-input").addEventListener('input',async (event)=>{
    const search = event.target.value;
    const dropdown = document.getElementById("from-dropdown");
    const airportList = document.getElementById("airport-list-from");

    if (search.trim() === "") {
        dropdown.style.display = "none";
        return;
    }
    try {
        ///asda
        const response=await fetch(`http://localhost:8686/api/AirPort/SearchAirPort?search=${encodeURIComponent(search)}`);
        console.log(response)
        if (response.ok) {
            const airports = await response.json();
            airportList.innerHTML = "";

            airports.forEach(airport => {
                const li = document.createElement("li");

                // Set the dropdown header with the country of the airport
                li.innerHTML = `
                    <div class="dropdown-header">
                        <span id="city-name">${airport.country}</span>
                        <span id="all-airports">Mọi sân bay</span>
                    </div>
                `;

                // Iterate over the airportDTOS and append each airport item
                airport.aiportDTOS.forEach(airportdto => {
                    const airportItem = document.createElement("div");
                    airportItem.classList.add("airport-item");
                    airportItem.innerHTML = `
                        <div class="airport-info">
                            <span class="airport-name"><i class="fa fa-plane icon" style="margin-right: 8px; color: #2a2a2a;"></i>Sân bay ${airportdto.name}</span>
                            <span class="airport-code">${airportdto.city.name}</span>
                        </div>
                    `;

                    // Add click event listener specifically to the 'airport-item' div
                    airportItem.addEventListener("click", () => {
                        document.getElementById("from-input").value = `${airportdto.city.name}`;

                        // Store the actual airport.id in the hidden input
                        document.getElementById("from-input-id").value = airportdto.id;
                        const toInputValue = document.getElementById("to-input").value;
                        if (toInputValue === document.getElementById("from-input").value) {
                            Swal.fire({
                                title:'Departure and arrival locations cannot be the same!',
                                icon:'error'
                            });
                            document.getElementById("from-input-id").value=0;
                            document.getElementById('from-input').value=''
                            return; // Prevent the selection if they are the same
                        }
                        dropdown.style.display = "none";
                    });

                    li.appendChild(airportItem);
                });

                airportList.appendChild(li);
            });

            dropdown.style.display = "block";
        }
    }catch (error){
        console.log(error)
    }
});
document.addEventListener('click', (event) => {
    const dropdown = document.getElementById('At-dropdown');
    const input = document.getElementById('At-input');
    if (!dropdown.contains(event.target) && !input.contains(event.target)) {
        dropdown.style.display = "none";
    }
});
document.addEventListener('DOMContentLoaded',async function (){
    const FromInputDropDown=document.querySelector('#from-input');
    const At=document.querySelector('#At-input');
    const City=document.querySelector('#idCity');
    const FromInput=document.querySelector('#from-input-id');
    const ArrivalInputDropDown=document.querySelector('#to-input');
    const ToInput=document.querySelector('#to-input-id');
    const loadingOverlay = document.createElement("div");
    console.log(FromInput.value)
    if (FromInput.value !== "0" && ToInput.value !== "0"){

        loadingOverlay.className = "loading-overlay";

        // Spinner element
        const spinner = document.createElement("div");
        spinner.className = "spinner";
        loadingOverlay.appendChild(spinner);

        // Optional loading text
        const loadingText = document.createElement("div");
        loadingText.className = "loading-text";
        loadingText.textContent = "Loading, please wait...";
        loadingOverlay.appendChild(loadingText);


        document.body.appendChild(loadingOverlay);
    }

    try {
        let fromAirports=await SearchById(FromInput.value);

        let ArrivalAirports=await SearchById(ToInput.value);
        let Cities=await SearchCityById(City.value);
        if(Cities!=null){
            At.value=Cities.name;
        }
        if(fromAirports!=null){
            FromInputDropDown.value=fromAirports.name;

        }
        if(ArrivalAirports!=null){
            ArrivalInputDropDown.value=ArrivalAirports.name;
        }
    }catch (error) {
        console.log(error);
    }finally {
        document.body.removeChild(loadingOverlay);
    }
})
async function SearchCityById(id){
    try {
        const response=await fetch(`http://localhost:8686/api/city/FindById/${encodeURIComponent(id)}`);
        let cities=null;
        if(response.ok){
            cities=await response.json();
        }
        return cities;
    }catch (error){
        console.log(error)
    }
}
async function SearchById( id){
    try {
        const response=await fetch(`http://localhost:8686/api/AirPort/FindById/${encodeURIComponent(id)}`);
        let airports=null;
        if(response.ok){
            airports=await response.json();

        }else {
            console.error(`Error: ${response.status} - ${response.statusText}`);
        }
        return airports;
    }catch (error){
        console.log(error)
    }
}
// Ngăn việc click bên trong dropdown bị đóng
document.getElementById('At-dropdown').addEventListener('click', (event) => {
    event.stopPropagation();
});
document.getElementById('At-input').addEventListener('input',async (event)=>{
    const search = event.target.value;
    const dropdown=document.getElementById('At-dropdown');
    const AtList=document.getElementById('At-List');
    if (search.trim() === "") {
        dropdown.style.display = "none";
        return;
    }
    try {
        const response=await fetch(`http://localhost:8686/api/city/SearchHotelByCityOrHotel?name=${encodeURIComponent(search)}`);
        if(response.ok){
            const airports = await response.json();
            AtList.innerHTML = "";
            airports.forEach(airport=>{

                const li = document.createElement("li");
                li.style.display='flex';
                li.innerHTML = `
                    <i class="fa-solid fa-star"></i>
                    ${airport.name}, ${airport.country.name}
                  
                   <span class="popular">Phổ biến</span>
                `;
                li.addEventListener("click",()=>{
                    document.getElementById("At-input").value=`${airport.name}`;
                    document.getElementById('idCity').value=airport.id;
                    dropdown.style.display = "none";
                })
                AtList.appendChild(li);
            })
            dropdown.style.display = "block";
        }
    }catch (error){
        console.log(error)
    }
})

document.getElementById("to-input").addEventListener("focus", function () {
    this.select(); // Highlight all text when input is focused
});
document.getElementById("from-input").addEventListener("focus", function () {
    this.select(); // Highlight all text when input is focused
});
document.getElementById("to-input").addEventListener('input', async (event) => {
    const search = event.target.value;
    const dropdown = document.getElementById("to-dropdown");
    const airportList = document.getElementById("airport-list");

    if (search.trim() === "") {
        dropdown.style.display = "none";
        return;
    }

    try {
        const response = await fetch(`http://localhost:8686/api/AirPort/SearchAirPort?search=${encodeURIComponent(search)}`);
        if (response.ok) {
            const airports = await response.json();
            airportList.innerHTML = "";

            airports.forEach(airport => {
                const li = document.createElement("li");

                // Set the dropdown header with the country of the airport
                li.innerHTML = `
                    <div class="dropdown-header">
                        <span id="city-name">${airport.country}</span>
                        <span id="all-airports">Mọi sân bay</span>
                    </div>
                `;

                // Iterate over the airportDTOS and append each airport item
                airport.aiportDTOS.forEach(airportdto => {
                    const airportItem = document.createElement("div");
                    airportItem.classList.add("airport-item");
                    airportItem.innerHTML = `
                        <div class="airport-info">
                            <span class="airport-name"><i class="fa fa-plane icon" style="margin-right: 8px; color: #2a2a2a;"></i>Sân bay ${airportdto.name}</span>
                            <span class="airport-code">${airportdto.city.name}</span>
                        </div>
                    `;

                    // Add click event listener specifically to the 'airport-item' div
                    airportItem.addEventListener("click", () => {
                        document.getElementById("to-input").value = `${airportdto.city.name}`;
                        document.getElementById("to-input-id").value = airportdto.id;
                        const FromInputValue = document.getElementById("from-input").value;
                        if(FromInputValue===document.getElementById("to-input").value){
                            Swal.fire({
                                title:'Departure and arrival locations cannot be the same!',
                                icon:'error'
                            })
                            document.getElementById('to-input-id').value=0;
                            document.getElementById("to-input").value='';
                        }

                        dropdown.style.display = "none"; // Hide dropdown after selection
                    });


                    li.appendChild(airportItem);
                });

                airportList.appendChild(li);
            });

            dropdown.style.display = "block";

        }
    } catch (error) {
        console.error("Error fetching airport data:", error);
    }
});

// Đóng dropdown khi nhấp ngoài
document.addEventListener("click", (event) => {
    const fromDropdown = document.getElementById("from-dropdown");
    const toDropdown = document.getElementById("to-dropdown");
    const searchFrom = document.getElementById("search-from");
    const searchTo = document.getElementById("search-to");

    // Ẩn dropdown "Bay từ" nếu nhấp ra ngoài
    if (!searchFrom.contains(event.target)) {
        fromDropdown.style.display = "none";
    }

    // Ẩn dropdown "Bay đến" nếu nhấp ra ngoài
    if (!searchTo.contains(event.target)) {
        toDropdown.style.display = "none";
    }
});

function initFlatpickr(selector, options) {
    // Khởi tạo flatpickr với selector và các tùy chọn
    flatpickr(selector, options);
}

function initDepartTime() {
    initFlatpickr("#departtime", {
        mode: "range", // Enable date range selection
        dateFormat: "Y-m-d", // Format to match LocalDate (yyyy-MM-dd)
        defaultDate: [new Date()], // Set default dates to today
        minDate: "today", // Chỉ cho phép chọn từ ngày hôm nay
        onReady: function (selectedDates, dateStr, instance) {
            const clearButton = document.createElement("button");
            clearButton.type = "button";
            clearButton.textContent = "Clear";
            clearButton.className = "flatpickr-clear-btn";
            clearButton.style.margin = "5px";
            clearButton.style.padding = "5px 10px";
            clearButton.style.backgroundColor = "#f44336";
            clearButton.style.color = "#fff";
            clearButton.style.border = "none";
            clearButton.style.cursor = "pointer";

            // Xóa các ngày đã chọn khi nhấn nút Clear
            clearButton.addEventListener("click", function () {
                instance.clear();
                instance.close(); // Đóng Flatpickr sau khi xóa
            });

            // Gắn nút Clear vào Flatpickr footer
            instance.calendarContainer.appendChild(clearButton);
            if (selectedDates.length) {
                const firstDate = instance.formatDate(selectedDates[0], "Y-m-d");
                instance.element.value = firstDate; // Set the input value to the first date
            }
        },
    });
}
function initCheckInTime() {
    initFlatpickr("#datePickerInputFrom", {
        mode: "range", // Enable date range selection
        dateFormat: "Y-m-d", // Format to match LocalDate (yyyy-MM-dd)
        defaultDate: [new Date()], // Set default dates to today
        minDate: "today", // Chỉ cho phép chọn từ ngày hôm nay
        onReady: function (selectedDates, dateStr, instance) {
            const clearButton = document.createElement("button");
            clearButton.type = "button";
            clearButton.textContent = "Clear";
            clearButton.className = "flatpickr-clear-btn";
            clearButton.style.margin = "5px";
            clearButton.style.padding = "5px 10px";
            clearButton.style.backgroundColor = "#f44336";
            clearButton.style.color = "#fff";
            clearButton.style.border = "none";
            clearButton.style.cursor = "pointer";

            // Xóa các ngày đã chọn khi nhấn nút Clear
            clearButton.addEventListener("click", function () {
                instance.clear();
                instance.close(); // Đóng Flatpickr sau khi xóa
            });

            // Gắn nút Clear vào Flatpickr footer
            instance.calendarContainer.appendChild(clearButton);
            if (selectedDates.length) {
                const firstDate = instance.formatDate(selectedDates[0], "Y-m-d");
                instance.element.value = firstDate; // Set the input value to the first date
            }
        },
    });
}
function initCheckOutTime() {
    const departTimeInput = document.getElementById("datePickerInputFrom");
    let departDateValue = departTimeInput.value;

    // Nếu departtime chưa được chọn, sử dụng ngày mai
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(today.getDate() + 1);

    // Nếu departtime đã được chọn, tính minDate từ giá trị này
    if (departDateValue) {
        const departDate = new Date(departDateValue);
        departDate.setDate(departDate.getDate() + 1); // Phải lớn hơn departtime 1 ngày
        departDateValue = departDate.toISOString().split('T')[0];
    } else {
        departDateValue = tomorrow.toISOString().split('T')[0];
    }

    initFlatpickr("#datePickerInputTo", {
        mode: "range", // Enable date range selection
        dateFormat: "Y-m-d", // Format to match LocalDate (yyyy-MM-dd)
        defaultDate: [new Date()], // Set default dates to today
        minDate: departDateValue, // Chỉ cho phép chọn từ ngày sau departtime
        onReady: function (selectedDates, dateStr, instance) {
            const clearButton = document.createElement("button");
            clearButton.type = "button";
            clearButton.textContent = "Clear";
            clearButton.className = "flatpickr-clear-btn";
            clearButton.style.margin = "5px";
            clearButton.style.padding = "5px 10px";
            clearButton.style.backgroundColor = "#f44336";
            clearButton.style.color = "#fff";
            clearButton.style.border = "none";
            clearButton.style.cursor = "pointer";

            // Xóa các ngày đã chọn khi nhấn nút Clear
            clearButton.addEventListener("click", function () {
                instance.clear();
                instance.close(); // Đóng Flatpickr sau khi xóa
            });

            // Gắn nút Clear vào Flatpickr footer
            instance.calendarContainer.appendChild(clearButton);
            if (selectedDates.length) {
                const firstDate = instance.formatDate(selectedDates[0], "Y-m-d");
                instance.element.value = firstDate; // Set the input value to the first date
            }
        },
    });
}

function initArrivalTime() {
    const departTimeInput = document.getElementById("departtime");
    let departDateValue = departTimeInput.value;

    // Nếu departtime chưa được chọn, sử dụng ngày mai
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(today.getDate() + 1);

    // Nếu departtime đã được chọn, tính minDate từ giá trị này
    if (departDateValue) {
        const departDate = new Date(departDateValue);
        departDate.setDate(departDate.getDate() + 1); // Phải lớn hơn departtime 1 ngày
        departDateValue = departDate.toISOString().split('T')[0];
    } else {
        departDateValue = tomorrow.toISOString().split('T')[0];
    }

    initFlatpickr("#arrivalTime", {
        mode: "range", // Enable date range selection
        dateFormat: "Y-m-d", // Format to match LocalDate (yyyy-MM-dd)
        defaultDate: [new Date()], // Set default dates to today
        minDate: departDateValue, // Chỉ cho phép chọn từ ngày sau departtime
        onReady: function (selectedDates, dateStr, instance) {
            const clearButton = document.createElement("button");
            clearButton.type = "button";
            clearButton.textContent = "Clear";
            clearButton.className = "flatpickr-clear-btn";
            clearButton.style.margin = "5px";
            clearButton.style.padding = "5px 10px";
            clearButton.style.backgroundColor = "#f44336";
            clearButton.style.color = "#fff";
            clearButton.style.border = "none";
            clearButton.style.cursor = "pointer";
            clearButton.addEventListener("click", function () {
                instance.clear();
                instance.close(); // Đóng Flatpickr sau khi xóa
            });

            // Gắn nút Clear vào Flatpickr footer
            instance.calendarContainer.appendChild(clearButton);
            if (selectedDates.length) {
                const firstDate = instance.formatDate(selectedDates[0], "Y-m-d");
                instance.element.value = firstDate; // Set the input value to the first date
            }
        },
    });
}

document.getElementById('datePickerInputFrom').addEventListener('focus',function (){
    if (!this.classList.contains("flatpickr-initialized")) {
        initCheckInTime();
        this.classList.add("flatpickr-initialized");
    }
});
document.getElementById('datePickerInputTo').addEventListener('focus',function (){
    if (!this.classList.contains("flatpickr-initialized")) {
        initCheckOutTime();
        this.classList.add("flatpickr-initialized");
    }
})
// Thêm sự kiện chỉ khởi tạo Flatpickr một lần khi focus vào input
document.getElementById("departtime").addEventListener("focus", function () {
    if (!this.classList.contains("flatpickr-initialized")) {
        initDepartTime();
        this.classList.add("flatpickr-initialized");
    }
});

document.getElementById("arrivalTime").addEventListener("focus", function () {
    if (!this.classList.contains("flatpickr-initialized")) {
        initArrivalTime();
        this.classList.add("flatpickr-initialized");
    }
});

document.getElementById('OneWay').addEventListener('click',function (){
    const OneWay=document.getElementById('OneWay');
    const RoundTrip=document.getElementById('RoundTrip');
    OneWay.style.color='#2067da';
    OneWay.style.backgroundColor='#e5efff';
    OneWay.style.borderColor='#2067da';
    RoundTrip.style.color='#24262c';
    RoundTrip.style.backgroundColor='#fff';
    RoundTrip.style.borderColor='rgba(94,107,130,.32)';
    document.getElementById('ArrivalDate').style.display='none';
    document.getElementById('TripTypeValue').value=false;
    const arrivalTimeInput = document.getElementById('arrivalTime');
    if (arrivalTimeInput) {
        arrivalTimeInput.value = ''; // Xóa giá trị đã chọn
        if (arrivalTimeInput._flatpickr) {
            arrivalTimeInput._flatpickr.clear(); // Xóa Flatpickr nếu đã được khởi tạo
        }
    }
})
document.getElementById('RoundTrip').addEventListener('click',function (){

    const RoundTrip=document.getElementById('RoundTrip');
    const OneWay=document.getElementById('OneWay');
    RoundTrip.style.color='#2067da';
    RoundTrip.style.backgroundColor='#e5efff';
    RoundTrip.style.borderColor='#2067da';
    OneWay.style.color='#24262c';
    OneWay.style.backgroundColor='#fff';
    OneWay.style.borderColor='rgba(94,107,130,.32)';
    document.getElementById('ArrivalDate').style.display='block';
    document.getElementById('TripTypeValue').value=true;

})
document.getElementById('flexSwitchCheckDefault').addEventListener('change',function (){
const AtHotel=document.getElementById('AtHotel');
const TimeHotel=document.getElementById('TimeHotel');
const RoomNumber=document.getElementById('RoomNumber');
if(this.checked){
    AtHotel.style.display='block';
    TimeHotel.style.display='block';
    RoomNumber.style.display='block';
}else {
    AtHotel.style.display='none';
    TimeHotel.style.display='none';
    RoomNumber.style.display='none'
}
})
document.querySelector('.swap-button').addEventListener('click', function () {
    const fromInput = document.getElementById('from-input');
    const toInput = document.getElementById('to-input');
    const fromInputId=document.getElementById('from-input-id');
    const ToInputId=document.getElementById('to-input-id');

    const tempValue = fromInput.value;
    fromInput.value = toInput.value;
    toInput.value = tempValue;
    const TempValueId=fromInputId.value;
    fromInputId.value=ToInputId.value;
    ToInputId.value=TempValueId;
});
