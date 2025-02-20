let selectedFiles = [];
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
document.addEventListener('DOMContentLoaded',()=>{
    document.getElementById('saveMultiple').addEventListener('click', function () {
        const saveButton = document.getElementById('saveMultiple');
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

        fetch(`http://localhost:8686/api/room/UpdateMultipleImage/${HotelId}`, {
            method: 'POST',

            body: formData,  // Pass formData directly as the body
        })
            .then(response => response.json())
            .then(data => {
                if (data.status === 200) {
                    Swal.fire(
                        'success!',
                        'The image is saved.',
                        'success'
                    ).then(() => {
                        // Reload the current page after successful deletion
                        window.location.reload();
                    });
                    selectedFiles = [];  // Reset the selected files array after successful upload
                } else {
                    Swal.fire(
                        'Deleted!',
                        'The Image is failed.',
                        'success'
                    ).then(() => {
                        // Reload the current page after successful deletion
                        saveButton.disabled = false;
                    });
                    alert('Failed to save images.');
                }
            })
            .catch(error => {
                console.error('Error uploading images:', error);
                alert('Error uploading images.');
            });
    });

    document.querySelectorAll('.delete-btn').forEach(button => {
        button.addEventListener('click', function() {
            const imageId = this.getAttribute('data-id');
            const hotelId = this.getAttribute('data-hotel-id');

            deleteImage(imageId, hotelId);
        });
    });



    const citySelect = new TomSelect("#city-select", {
        placeholder: "Select a city",
        onChange: function (cityId) {
            // Load the districts when city changes

        }
    });




})
const deleteButtons = document.querySelectorAll('#delete-btn'); // Lấy tất cả các nút có lớp .delete-btn
this.disabled = true;
for (let i = 1; i < deleteButtons.length; i++) { // Lặp qua danh sách nút
    deleteButtons[i].addEventListener('click', function () {
        const imageId = this.getAttribute('data-id'); // Lấy giá trị imageId từ thuộc tính data-id
        const hotelId = this.getAttribute('data-hotel-id'); // Lấy giá trị hotelId từ thuộc tính data-hotel-id

        if (!imageId) {
            Swal.fire(
                'Error!',
                'Invalid image ID.',
                'error'
            );
            this.disabled = false;
            return;
        }

        deleteImage(imageId, hotelId); // Gọi hàm deleteImage với imageId và hotelId
    });
}

function deleteImage(imageId, hotelId) {
    console.log(imageId)
    // Make the API request to delete the image
    fetch(`http://localhost:8686/api/room/DeletePictureImage/${imageId}`, {
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
                );
                window.location.reload();
            }
        })
        .catch(error => {
            console.error('Error deleting image:', error);

            window.location.reload();
        });
}
