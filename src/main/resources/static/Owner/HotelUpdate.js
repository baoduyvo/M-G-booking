let htmlContent = document.getElementById('editorAddress').value;

var map = L.map('map').setView([51.505, -0.09], 13);
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);
var markers=[];
function clearMarkers(){
    markers.forEach(markers=>{
        map.removeLayer(markers);
    });
    markers=[];
}
function searchAddress(query){
    var url=`https://nominatim.openstreetmap.org/search?format=json&q=${query}`;
    fetch(url)
        .then(response=>response.json())
        .then(data=>{
            if(data.length>0){
                clearMarkers();
                var lat=parseFloat(data[0].lat);
                var lon=parseFloat(data[0].lon);
                map.setView([lat,lon],13);
                var newMarker=L.marker([lat,lon]).addTo(map)
                    .bindPopup(data[0].display_name)
                    .openPopup();
                markers.push(newMarker);
            }
        })
}
function compareAddressEditor(address){
    searchAddress(address)
}
compareAddressEditor(htmlContent)

let selectedFiles = [];
document.addEventListener('DOMContentLoaded',()=>{
    document.getElementById('saveMultiple').addEventListener('click', function () {
        const saveButton = document.getElementById('saveMultiple');
        saveButton.disabled = true;
        if (selectedFiles.length === 0) {
            alert('No images to save');
            return;
        }

        const formData = new FormData();

        // Append each file to FormData individually
        selectedFiles.forEach(file => {
            formData.append('MultiImage', file);  // Append each file individually under 'MultiImage'
        });

        const token = document.getElementById('token') ? document.getElementById('token').textContent : null;
        const HotelId = this.getAttribute('dataHotel-id');

        fetch(`http://localhost:8686/api/hotel/UpdateMultipleImage/${HotelId}`, {
            method: 'POST',

            body: formData,  // Pass formData directly as the body
        })
            .then(response => response.json())
            .then(data => {
                if (data.status === 200) {
                    Swal.fire(
                        'Deleted!',
                        'The image has been deleted.',
                        'success'
                    ).then(() => {
                        // Reload the current page after successful deletion
                        window.location.reload();
                    });
                    selectedFiles = [];
                } else {
                    Swal.fire(
                        'Error!',
                        'There was an issue deleting the image.',
                        'error'
                    );
                    saveButton.disabled=false;
                }
            })
            .catch(error => {
                console.error('Error uploading images:', error);
                alert('Error uploading images.');
            });
    });

    document.querySelectorAll('.delete-btn').forEach(button => {
        button.addEventListener('click', function() {
            this.disabled = true;
            const imageId = this.getAttribute('data-id');
            const hotelId = this.getAttribute('data-hotel-id');

            deleteImage(imageId, hotelId);
        });
    });

    function deleteImage(imageId, hotelId) {

        // Make the API request to delete the image
        fetch(`http://localhost:8686/api/hotel/DeletePictureImage/${imageId}`, {
            method: 'DELETE',


        })
            .then(response => response.json())
            .then(data => {
                if (data.status === 200) {
                    // Show success message
                    Swal.fire(
                        'Deleted!',
                        'The image has been deleted.',
                        'success'
                    ).then(() => {
                        // Redirect to the desired page after a successful deletion
                        window.location.reload();
                    });
                } else {
                    // Show error message
                    Swal.fire(
                        'Error!',
                        'There was an issue deleting the image.',
                        'error'
                    ).then(() => {
                        // Re-enable the button in case of failure
                        button.disabled = false;
                    });
                }
            })
            .catch(error => {
                console.error('Error deleting image:', error);
                Swal.fire(
                    'Error!',
                    'An error occurred while deleting the image.',
                    'error'
                );
            });
    }

    const citySelect = new TomSelect("#city-select", {
        placeholder: "Select a city",
        onChange: function (cityId) {
            // Load the districts when city changes

        }
    });




})
const fileInput = document.getElementById('fileInput');
const uploadArea = document.getElementById('uploadArea');
const imagePreviewContainer = document.getElementById('imagePreviewContainer');
uploadArea.addEventListener('click', () => {
    fileInput.click(); // Mở hộp thoại chọn file
});
fileInput.addEventListener('change', (event) => {
    const files = event.target.files;
    imagePreviewContainer.innerHTML = ''; // Xóa preview cũ
    console.log(files)
    if (files.length > 0) {
        const file = files[0]; // Chỉ lấy file đầu tiên

        if (file.type.startsWith('image/')) {
            const reader = new FileReader();

            // Đọc file và tạo bản xem trước
            reader.onload = (e) => {
                const imageDiv = document.createElement('div');
                imageDiv.className = 'image-preview';
                imageDiv.style.backgroundImage = `url(${e.target.result})`;

                const img = document.createElement('img');
                img.src = e.target.result;
                imageDiv.appendChild(img);

                // Thêm vào container (chỉ 1 hình ảnh)
                imagePreviewContainer.appendChild(imageDiv);
            };

            reader.readAsDataURL(file);
        } else {
            console.error('File không phải là hình ảnh:', file.name);
        }
    }
});

document.getElementById("fileInputArea").addEventListener("change",function (event){
    const imagePreviewContainer=document.getElementById("imagePreviewContainerArea");
    imagePreviewContainer.innerHTML="";
    const files=event.target.files;
    if (files.length === 0) {
        imagePreviewContainer.innerHTML = "<p>No files selected.</p>";
        return;
    }
    Array.from(files).forEach((file)=>{
        if(file.type.startsWith("image/")){
            const reader = new FileReader();
            reader.onload = (e) => {
                const img = document.createElement("img");
                img.src = e.target.result;
                img.alt = file.name;
                img.style.width = "100px";
                img.style.margin = "5px";
                img.className = "uploaded-image-preview";
                imagePreviewContainer.appendChild(img);
            };
            reader.readAsDataURL(file);
        }else{
            const error = document.createElement("p");
            error.textContent = `File "${file.name}" is not an image.`;
            error.style.color = "red";
            imagePreviewContainer.appendChild(error);
        }
    })
})

function addNewImage() {
    // Mở hộp thoại chọn file hình ảnh
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.id="MultiImage";
    fileInput.accept = 'image/*';
    fileInput.onchange = (event) => {
        const file = event.target.files[0];
        if (file) {
            selectedFiles.push(file);
            const imageContainer = document.getElementById('image-container');
            const reader = new FileReader();
            reader.onload = function (e) {
                // Tạo HTML cho hình ảnh mới


                // Thêm hình ảnh mới
                const newImageItem = document.createElement('div');
                newImageItem.classList.add('image-item');
                newImageItem.innerHTML = `
          <img src="${e.target.result}" alt="New Image" class="product-image">
          <button type="button" class="delete-btn" onclick="deleteImage(this)">
            <i class="fa fa-trash-alt" style="color: black"></i>
          </button>
        `;
                imageContainer.appendChild(newImageItem);

                // Di chuyển nút "Add Image" tới vị trí cuối
                const addImageContainer = document.querySelector('.add-image-container');
                imageContainer.appendChild(addImageContainer);
            };
            reader.readAsDataURL(file); // Đọc file và chuyển sang URL base64
        }
    };
    fileInput.click();
}
function deleteImage(button) {
    // Tìm phần tử cha (image-item) chứa nút xóa
    const imageItem = button.closest('.image-item');

    if (imageItem) {
        // Hiển thị xác nhận trước khi xóa (tùy chọn)
        const confirmDelete = confirm('Bạn có chắc muốn xóa hình ảnh này?');
        if (confirmDelete) {
            // Xóa phần tử hình ảnh khỏi DOM
            imageItem.remove();

            // Tìm khung "Add Image" kế tiếp sau hình ảnh vừa xóa
            const nextAddImageContainer = imageItem.nextElementSibling;
            if (nextAddImageContainer && nextAddImageContainer.classList.contains('add-image-container')) {
                // Xóa khung "Add Image" nếu nó tồn tại
                nextAddImageContainer.remove();
            }
        }
    }
}


