document.addEventListener("DOMContentLoaded", function() {
const CountryId=document.getElementById('country-select').value
    const CitySelect=document.getElementById('city-select');

    if(CountryId){
        fetch(`http://localhost:8686/api/city/FindCityByCountry/${CountryId}`)
            .then(response=>{
                if(!response.ok){
                    throw new Error('Failed to fetch cities');
                }
                return response.json();
            })
            .then(data=>{
                data.forEach(city=>{
                    const option=document.createElement('option');
                    option.value=city.id;
                    option.textContent=city.name;
                    CitySelect.appendChild(option);
                });
            })
            .catch(error=>{
                console.error('Error fetching cities:', error);
            })
    }
})
document.getElementById('country-select').addEventListener('change',function (){
    const countryId=this.value;
    const CitySelect=document.getElementById('city-select');
    CitySelect.innerHTML='<option value="">Select a city</option>';
    if(countryId){
        fetch(`http://localhost:8686/api/city/FindCityByCountry/${countryId}`)
            .then(response=>{
                if(!response.ok){
                    throw new Error('Failed to fetch cities');
                }
                return response.json();
            })
            .then(data=>{
                data.forEach(city=>{
                    const option=document.createElement('option');
                    option.value=city.id;
                    option.textContent=city.name;
                    CitySelect.appendChild(option);
                });
            })
            .catch(error=>{
                console.error('Error fetching cities:', error);
            })
    }
})
document.getElementById('avatarUpload').addEventListener('click',function (){
    document.getElementById('avatarInput').click();
})
document.getElementById('avatarInput').addEventListener('change',function (event){
    const file=event.target.files[0];
    if(file){
        const reader=new FileReader();
        reader.onload=function (e){
            const avatarImage=document.getElementById('avatarImage');
            const placeHolder=document.getElementById('placeholder');
            avatarImage.src = e.target.result;
            avatarImage.style.display = "block";
           placeHolder.style.display = "none";
        }
        reader.readAsDataURL(file);
    }
})
document.getElementById("frontUpload").addEventListener("click", function () {
    document.getElementById("frontInput").click();
});
document.getElementById('frontInput').addEventListener('change',function (event){
    const file=event.target.files[0];
    if(file){
        const reader=new FileReader();
        reader.onload=function (e){
            const frontImage = document.getElementById("frontImage");
            frontImage.src = e.target.result;
            frontImage.style.display = "block";

        };
        reader.readAsDataURL(file);
    }
})
document.getElementById("backUpload").addEventListener("click", function () {
    document.getElementById("backInput").click();
});
document.getElementById("backInput").addEventListener("change", function (event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();

        reader.onload = function (e) {
            const backImage = document.getElementById("backImage");
            const backPlaceholder = document.getElementById("backPlaceholder");

            backImage.src = e.target.result;
            backImage.style.display = "block";
            backPlaceholder.style.display = "none";
        };

        reader.readAsDataURL(file);
    }
});
