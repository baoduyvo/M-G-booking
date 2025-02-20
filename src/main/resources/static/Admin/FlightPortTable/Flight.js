const openModalBtns = document.querySelectorAll('.open-modal-btn');
const modal = document.getElementById('customModal');
const closeModalBtn = document.getElementById('btnClose');
const CloseBtn=document.getElementById('closeModalBtn');
const cancelModalBtn = document.getElementById('cancelModalBtn');
const DetailSeat=document.querySelectorAll('#DetailSeat');
document.getElementById('seatForm').addEventListener('submit',function (event){
    const firstClassSeat=document.getElementById('FirstSeat');
    const BusinessSeat=document.getElementById('BusinessSeat');
    const EconomySeat=document.getElementById('EconomySeat');
    if (firstClassSeat.value.trim() === "") {
        firstClassSeat.value = 0; // Gán giá trị mặc định là 0
    }
    if (BusinessSeat.value.trim() === "") {
        BusinessSeat.value = 0; // Gán giá trị mặc định là 0
    }
    if (EconomySeat.value.trim() === "") {
        EconomySeat.value = 0; // Gán giá trị mặc định là 0
    }
})



openModalBtns.forEach((btn) => {
    btn.addEventListener('click', function(event) {
        event.preventDefault(); // Ngừng việc chuyển hướng trang
        const flightId = this.getAttribute('data-idFlight');


        fetch(`http://localhost:8686/api/seat/existBySeat/${flightId}`)
            .then(response => response.json())
            .then(data => {
                // Render and display seat data
                if(!data){
                    modal.style.display = 'flex';
                    modal.style.zIndex='10000';
                    document.getElementById('idFlight').value=flightId;
                }else{
                    Swal.fire({
                        title:"Flight has created seats",
                        icon:'error',
                        
                    })
                }



            })
            .catch(error => console.error('Error fetching seat details:', error));
        // Lấy id chuyến bay từ data-idFlight và truyền vào form nếu cần

    });
});
CloseBtn.addEventListener('click',function (event){
    event.preventDefault();
    const seatGrid=document.querySelector('.seat-grid');
    if(seatGrid){
        seatGrid.innerHTML='';
    }
    document.getElementById('FirstSeat').value=0;
    document.getElementById('BusinessSeat').value=0;
    document.getElementById('EconomySeat').value=0;
    modal.style.display='none';
})
DetailSeat.forEach((btn) => {
    btn.addEventListener('click', function (event) {
        const idFlight = this.getAttribute('data-idFlight');
        const DepartAirPort=this.getAttribute('data-departAirport');
        const ArrivalAirPort=this.getAttribute('data-arrivalAirport');
        document.getElementById('location').textContent=`${DepartAirPort} -${ArrivalAirPort}`;

        fetch(`http://localhost:8686/api/seat/existBySeat/${idFlight}`)
            .then(response => response.json())
            .then(data => {
                // Render and display seat data
                if(data){
                    fetch(`http://localhost:8686/api/seat/${idFlight}`)
                        .then(response => response.json())
                        .then(data => {
                            // Render and display seat data
                            renderSeatDetails(data);

                            // Show the modal (ensure you implement modal logic here if needed)
                            document.getElementById('seatDetail').style.display = 'flex';
                        })
                        .catch(error => console.error('Error fetching seat details:', error));
                }else{
                    Swal.fire({
                        title:"flight not available",
                        icon:'error',

                    })
                }



            })
            .catch(error => console.error('Error fetching seat details:', error));

    });
});
closeModalBtn.addEventListener('click',function (event){

    event.preventDefault();

    document.getElementById('seatDetail').style.display = 'none';
})
// Function to render seat details dynamically
function renderSeatDetails(data) {
    const seatGrid = document.querySelector('#seatDetail .seat-grid');

    if (!seatGrid) {
        console.error("seatGrid element not found!");
        return;
    }

    seatGrid.innerHTML = ''; // Clear existing seat details

    // Add seat header (A, B, C, Row, D, E, F)
    const headerRow = document.createElement('div');
    headerRow.style.display = 'flex';
    headerRow.style.alignItems = 'center';
    headerRow.style.justifyContent = 'center';
    headerRow.style.marginBottom = '10px';
    headerRow.style.gap = '10px';

    const headers = ['A', 'B', 'C', '', 'D', 'E', 'F'];
    headers.forEach(label => {
        const headerDiv = document.createElement('div');
        headerDiv.style.width = '50px';
        headerDiv.style.textAlign = 'center';
        headerDiv.style.fontWeight = 'bold';
        headerDiv.textContent = label;
        headerRow.appendChild(headerDiv);
    });

    seatGrid.appendChild(headerRow); // Append header to seatGrid

    // Create seat rows
    let currentRowNumber = 0;
    let rowDiv; // Declare rowDiv outside the loop
    data.forEach((seat, index) => {
        // Create a new row if it's the first seat or every 6 seats (excluding row number)
        if (index % 6 === 0) {
            if (rowDiv) {
                seatGrid.appendChild(rowDiv); // Append the previous row before creating a new one
            }

            rowDiv = document.createElement('div'); // Create a new row
            rowDiv.style.display = 'flex';
            rowDiv.style.alignItems = 'center';
            rowDiv.style.marginBottom = '5px';
            rowDiv.style.gap = '10px';

            currentRowNumber++;
        }

        // Add row number in the middle (between C and D)
        if (index % 6 === 3) {
            const rowNumberDiv = document.createElement('div');
            rowNumberDiv.style.width = '50px';
            rowNumberDiv.style.textAlign = 'center';
            rowNumberDiv.style.fontWeight = 'bold';
            rowNumberDiv.textContent = currentRowNumber;
            rowDiv.appendChild(rowNumberDiv);
        }

        // Create seat element
        const seatDiv = createSeatDiv(seat);

        // Add seat to the current row
        rowDiv.appendChild(seatDiv);
    });

    // Append the last row
    if (rowDiv) {
        seatGrid.appendChild(rowDiv);
    }
}

// Helper function to create a seat div
function createSeatDiv(seat) {
    const seatDiv = document.createElement('div');
    seatDiv.style.width = '50px';
    seatDiv.style.height = '50px';
    seatDiv.style.borderRadius = '4px';
    if(seat.status==1){
        seatDiv.style.backgroundColor='#2ecc71'
    }else{
        seatDiv.style.backgroundColor =
            seat.type === 'First Class' ? '#a7d2ff' :
                seat.type === 'Business Class' ? '#7199ff' :
                    seat.type === 'Economy Class' ? '#2c4aff' : '#dcdcdc';
    }

    seatDiv.title = `Seat ID: ${seat.id}`;
    seatDiv.dataset.seatId = seat.id;
    return seatDiv;
}
document.getElementById('generateSeats').addEventListener('click',()=>{
    const firstClassSeats=parseInt(document.getElementById('FirstSeat').value)||0;
    const BusinessClassSeats=parseInt(document.getElementById('BusinessSeat').value)||0;
    const EconomyClassSeats=parseInt(document.getElementById('EconomySeat').value)||0;
    const totalSeats = firstClassSeats + BusinessClassSeats + EconomyClassSeats;
    const totalRows = Math.ceil(totalSeats / 6);
    const seatData = generateSeatData(firstClassSeats, BusinessClassSeats,EconomyClassSeats, totalRows);
    renderSeatGrid(seatData);
})
function generateSeatData(firstClassSeats,businessClassSeats,economyClassSeats,rows){
    const seatTypes=[];
    for(let i=0;i<firstClassSeats;i++)seatTypes.push('First Class');
    for (let i=0;i<businessClassSeats;i++)seatTypes.push('Business Class');
    for (let i=0;i<economyClassSeats;i++)seatTypes.push('Economy Class');
    return {rows,cols:['A','B','C','D','E','F'],seatTypes};
}
function renderSeatGrid(data){
    const seatGrid=document.querySelector('.seat-grid');
    seatGrid.innerHTML='';
    let seatIndex=0;
    for(let row=1;row<=data.rows;row++){
        const rowDiv=document.createElement('div');
        rowDiv.classList.add('seat-row');

        data.cols.forEach((col, colIndex)=>{
            if (col === 'D') {
                const indexDiv = document.createElement('div');
                indexDiv.classList.add('seat-index');
                indexDiv.textContent = row; // Hiển thị số thứ tự theo hàng
                indexDiv.style.width = '30px';
                indexDiv.style.textAlign = 'center';
                rowDiv.appendChild(indexDiv);
            }
            const seatDiv=document.createElement('div');
            seatDiv.classList.add('seat');

            if (seatIndex < data.seatTypes.length) {
                const seatType = data.seatTypes[seatIndex];
                seatDiv.textContent = col;
                seatDiv.classList.add(
                    seatType === 'First Class'
                        ? 'first-class'
                        : seatType === 'Business Class'
                            ? 'business-class'
                            : 'economy-class'
                );
                seatIndex++;
            } else {
                // Nếu không còn ghế, tạo một phần tử trống hoặc bỏ qua

            }
            rowDiv.appendChild(seatDiv);
        });
        seatGrid.appendChild(rowDiv)
    }
}