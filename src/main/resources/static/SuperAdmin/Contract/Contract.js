function handleContractClick(button) {
    button.disabled=true;
    const contractOwnerId = button.getAttribute('data-contract-owner-id');
    const contractOwnerName = button.getAttribute('data-owner-name');
    const email = button.getAttribute('data-email');
    const id = button.getAttribute('data-id');
    const phone = button.getAttribute('data-phone');
    const idHotel = button.getAttribute('data-id-hotel');
    const hotelName = button.getAttribute('data-hotel-name');
    const address = button.getAttribute('data-address');

    const contractOwnerDTO = {
        ownerId: contractOwnerId,
        id: id,
        ownerName: contractOwnerName,
        email: email,
        phone: phone,
        address: address,
        idHotel: idHotel,
        hotelName: hotelName
    };

    // Make the API call or do something else
    fetch('http://localhost:8686/api/ContractOwner/AcceptContract', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(contractOwnerDTO)
    }).then(response => response.json())
        .then(data => {
            window.location.reload();
        })
        .catch(error => {
            console.error('Error:', error);
            button.disabled = false;
            alert('An error occurred. Please try again.');
        });
}
