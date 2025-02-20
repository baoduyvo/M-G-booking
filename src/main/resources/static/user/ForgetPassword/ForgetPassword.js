$(document).ready(function () {
    let countdownInterval;
    $(".otp-box").on("keyup",function (e){
        if(e.key>=0 && e.key<=9){
            $(this).next(".otp-box").focus();
        }else if(e.key==="Backspace"){
            $(this).prev(".otp-box").focus();
        }
    });
    $("#verifyOtpButton").click(function (){
        let otp = "";
        var submitButton = $(this).find('.button-text');
        var originalText = "<span class='button-text'>Verify OTP</span>";

        submitButton.html('<span class="spinner" style="display: flex"></span>');
        $(".otp-box").each(function () {
            otp += $(this).val();
        });
        var Email = $('#email').val();
        let otpbox={
            otp:otp,
            email:Email
        }

        $.ajax({
            url: "http://localhost:8686/api/CheckOTP",
            type:"POST",
            contentType: 'application/json', // Specify JSON format
            data: JSON.stringify(otpbox),
            success:function (response){
                if(response.status===200){
                    document.getElementById("otpPopup").style.display = 'none';
                    showChangePasswordPopup();
                }
            },
            error: function (xhr, status, error) {
                // Log and display any errors that occurred during the request
                Swal.fire({
                    icon:'error',
                    title:'OTP is valid',
                })
                submitButton.html(originalText);
                submitButton.prop('disabled', false);

            }
        })
    })
    function showChangePasswordPopup() {

        $('#changePasswordModal').modal('show');
    }
    $('#resendOTP').click(function (){
        var Email = $('#email').val();
        $.ajax({
            url: 'http://localhost:8686/ResendOTP',
            type:'POST',
            data:JSON.stringify({
                email: Email
            }),
            success:function (response){
                if(response.status===200){
                    StartCountDown(120)
                    const verifyOtpButton=document.getElementById("resendOTP");
                    verifyOtpButton.innerText="Verify OTP";
                    verifyOtpButton.id="verifyOtpButton";

                }
            }
        })
    })
    // Attach click event to the submit button
    $('#submitButton').click(function () {
        var submitButton = $('#submitButton');
        var originalText = "Reset Password <i class='fa-solid fa-arrow-up-right-from-square' style='margin-left: 15px'></i>";
        // Get the email value from the input field
        var Email = $('#email').val();

        // Create the DTO object to send in the request
        var forgetDTO = {
            email: Email
        };
        submitButton.html('<span class="spinner"></span> Processing...');
        submitButton.prop('disabled', true);
        // Make the AJAX request
        $.ajax({
            url: 'http://localhost:8686/api/ForgetPassword', // API endpoint
            type: 'POST',           // HTTP method
            contentType: 'application/json', // Specify JSON format
            data: JSON.stringify(forgetDTO), // Convert DTO to JSON string
            success: function (response) {
                console.log(response)
                // Check if the API response status is 200 (success)
                if (response.status === 200) {
                    submitButton.html(originalText);
                    submitButton.prop('disabled', false);
                    // Display the OTP popup if the request is successful
                    document.getElementById("otpPopup").style.display = 'flex';
                     StartCountDown(120)
                } else {
                    submitButton.html(originalText);
                    submitButton.prop('disabled', false);
                    // Handle unexpected responses
                   Swal.fire({
                       icon:'error',
                       title:'Account does not exist',
                   })
                }
            },
            error: function (xhr, status, error) {
                // Log and display any errors that occurred during the request
                Swal.fire({
                    icon:'error',
                    title:'Account does not exist',
                })
                submitButton.html(originalText);
                submitButton.prop('disabled', false);
            },

        });
    });
    function StartCountDown(seconds){
        let remainingTime=seconds;
        updateCountdownDisplay(remainingTime);
        clearInterval(countdownInterval)
        countdownInterval=setInterval(function (){
            remainingTime--;
            updateCountdownDisplay(remainingTime);
            if(remainingTime<=0){
                clearInterval(countdownInterval);
                const verifyOtpButton=document.getElementById("verifyOtpButton");
                verifyOtpButton.innerText="Resend OTP";
                verifyOtpButton.id="resendOTP";

            }
        },1000);
    }
    function updateCountdownDisplay(time){
        $('#countdown').text(`Time remaining: ${time} seconds`);
    }
    $('#submitNewPassword').click(function (){
        var Email = $('#email').val();
        var newPassword=$('#newPassword').val();
        var confirmPassword=$('#confirmPassword').val();
        var errorMessages=[];
        if(newPassword.length<6){
            errorMessages.push("Password must be at least 6 characters long.");
        }
        if (newPassword !== confirmPassword) {
            errorMessages.push("Passwords do not match.");
        }
        if (errorMessages.length > 0) {

            var errorHtml = "";
            errorMessages.forEach(function (message) {
                errorHtml += "<p>" + message + "</p>";
            });

            $('#validationErrors').html(errorHtml);
            return; // Dừng xử lý
        }
        $.ajax({
            url: "http://localhost:8686/api/ChangePassword",
            type:"POST",
            contentType: 'application/json',
            data:JSON.stringify({
                Password:newPassword,
                Email:Email
            }),
            success:function (response){
                if(response.status===200){
                    window.location.href = "http://localhost:8686/Login";
                }
            }
        })

    })
});
