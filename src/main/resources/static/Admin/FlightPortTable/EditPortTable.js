document.addEventListener("DOMContentLoaded", function () {
      var departureTimeInput = document.getElementById("datetimepicker");


    var dateValue = departureTimeInput.value.replace('Z', '');
    flatpickr("#datetimepicker", {
        enableTime: true,
        dateFormat: "Y-m-d\\TH:i",  // Date and time format without UTC 'Z'
        altInput: true,               // Use alternate input for user-friendly display
        altFormat: "F j, Y, H:i",     // User-friendly display format (e.g., "November 28, 2024, 19:00")
        time_24hr: true,              // 24-hour format
        defaultDate: dateValue,  // Default local time (e.g., HCM time)
        timezone: "Asia/Ho_Chi_Minh", // Set the time zone to HCM
    });
});


document.addEventListener("DOMContentLoaded", function () {
    var departureTimeInput = document.getElementById("Departure_Time");


    var dateValue = departureTimeInput.value.replace('Z', '');
    flatpickr("#Departure_Time", {
        enableTime: true,
        dateFormat: "Y-m-d\\TH:i",  // Date and time format without UTC 'Z'
        altInput: true,               // Use alternate input for user-friendly display
        altFormat: "F j, Y, H:i",     // User-friendly display format (e.g., "November 28, 2024, 19:00")
        time_24hr: true,              // 24-hour format
        defaultDate: dateValue,  // Default local time (e.g., HCM time)
        timezone: "Asia/Ho_Chi_Minh",
        // Set the time zone to HCM
    });
});
document.addEventListener('DOMContentLoaded', function () {
    const editButtons = document.querySelectorAll('.edit-btn');
   // Debugging line, can be removed after testing
    const SaveButtons=document.querySelectorAll(".save-btn");
    const saveBtnFlight=document.querySelector("#saveButton");

    editButtons.forEach(button => {

        button.addEventListener('click', function () {
            const id = this.getAttribute('data-id');

            const EditBtn=document.getElementById(`editBtn${id}`);
            const saveBtn=document.getElementById(`saveBtn${id}`)

            // Get input and label elements
            const typeInput = document.getElementById(`type-${id}`);
            const priceInput = document.getElementById(`price-${id}`);
            const quantityInput = document.getElementById(`quantity-${id}`);
            const typeLabel = document.getElementById(`type-label-${id}`);
            const priceLabel = document.getElementById(`price-label-${id}`);
            const quantityLabel = document.getElementById(`quantity-label-${id}`);


                // Hide inputs, show labels
                typeInput.style.display = "inline-block";
                priceInput.style.display = "inline-block";
                quantityInput.style.display = "inline-block";
                typeLabel.style.display = "none";
                priceLabel.style.display = "none";
                quantityLabel.style.display = "none";

            EditBtn.style.setProperty('display', 'none', 'important');
            saveBtn.style.setProperty('display','inline-block','important')
            saveBtnFlight.style.opacity='0.5';
            saveBtnFlight.style.cursor="not-allowed"
        });
    });
    SaveButtons.forEach(buttons =>{

        buttons.addEventListener('click',function (){
            const id = this.getAttribute('data-id');
            const EditBtn=document.getElementById(`editBtn${id}`);
            const saveBtn=document.getElementById(`saveBtn${id}`)
            // Get input and label elements
            const typeInput = document.getElementById(`type-${id}`);
            const saveBtnFlight=document.querySelector("#saveButton");
            const priceInput = document.getElementById(`price-${id}`);
            const quantityInput = document.getElementById(`quantity-${id}`);
            const typeLabel = document.getElementById(`type-label-${id}`);
            const priceLabel = document.getElementById(`price-label-${id}`);
            const quantityLabel = document.getElementById(`quantity-label-${id}`);
            const updatedData = {
                id: id,
                type: typeInput.value,
                price: priceInput.value,
                quantity: quantityInput.value
            };
            typeLabel.textContent = updatedData.type;
            priceLabel.textContent = updatedData.price;
            quantityLabel.textContent = updatedData.quantity;

            // Hide inputs and show labels
            typeInput.style.display = 'none';
            priceInput.style.display = 'none';
            quantityInput.style.display = 'none';

            typeLabel.style.display = 'inline-block';
            priceLabel.style.display = 'inline-block';
            quantityLabel.style.display = 'inline-block';
            EditBtn.style.setProperty('display', 'inline-block', 'important');
            saveBtn.style.setProperty('display','none','important')
            saveBtnFlight.style.opacity='1';
            saveBtnFlight.style.cursor="pointer"
        })
    })
});
document.addEventListener('DOMContentLoaded', function () {
    const successMessage = sessionStorage.getItem('successMessage');
    if (successMessage) {
        // Hiển thị toastr
        toastr.success(successMessage, 'Success');
        // Xóa thông báo sau khi hiển thị
        sessionStorage.removeItem('successMessage');
    }
});

$(document).ready(function (){
    $('#saveButton').click(function (){
        var flightDate=[];
        $('tr').each(function (){
            var row=$(this);
            var id=row.find("input[name*='id']").val();
            var idFlight=row.find("input[name*='idFlight']").val();
            var type=row.find("select[name*='.type']").val();
            var quantity=row.find("input[name*='.quantity']").val();
            var price = row.find("input[name*='.price']").val();
            if (id) {
                flightDate.push({
                    id: id,
                    type: type,
                    quantity: quantity,
                    price: price,
                    idFlight:idFlight
                });

            }
        });
        const url = document.getElementById('url') ? document.getElementById('url').textContent : null;
        const token = document.getElementById('token') ? document.getElementById('token').textContent : null;
        $.ajax({
            url: `http://localhost:8686/api/DetailFlight/UpdateDetail`,
            type:'PUT',
            contentType: 'application/json',
            headers: {
                'Authorization': 'Bearer ' + token,
            },
            data: JSON.stringify(flightDate),
            success:function (response){

                if(response.status==200){

                    sessionStorage.setItem('successMessage', response.message);

                    // Reload lại trang
                    window.location.reload();
                }else{
                    toastr.error(response.message,'Error');
                }
            },
            error: function (xhr, status, error) {
                console.error('Error:', status, error);
                alert('Something went wrong!');
            }
        })
    })
})
document.getElementById("addDetailFlight").addEventListener('click',function (){
    const modal=document.getElementById("addDetailFlightModal");
    modal.style.display='block';
})