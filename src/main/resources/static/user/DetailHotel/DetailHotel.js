
let CurrentIndextText = 0; // Dùng 'let' thay vì 'const'

const prevnext = document.querySelector('#prevprev1');
const nextnext = document.querySelector('#nextnext1');

prevnext.addEventListener('click', () => {
    CurrentIndextText--; // Giảm chỉ số slide
    showSlide(CurrentIndextText);
});

nextnext.addEventListener('click', () => {
    CurrentIndextText++; // Tăng chỉ số slide
    showSlide(CurrentIndextText);
});


function currentSlideIndex(index) {
    showSlide(index);
}

function showSlide(index) {
    let currentIndex = 0;
    const slides = document.querySelector('#slides');
    const totalSlides = document.querySelectorAll('#slide').length;
    const dots = document.querySelectorAll('.dot');
    const allSlides = document.querySelectorAll('.slide');
    const captions = document.querySelectorAll('.caption');
    if (index < 0) {
        currentIndex = totalSlides - 1;
    } else if (index >= totalSlides) {
        currentIndex = 0;
    } else {
        currentIndex = index;
    }
    const offset = -currentIndex * 100;
    slides.style.transform = `translateX(${offset}%)`;

    // Cập nhật trạng thái dot active
    dots.forEach((dot, i) => {
        dot.classList.toggle('active', i === currentIndex);
    });

    // Chỉ hiển thị caption của slide đang hiển thị
    captions.forEach((caption, i) => {
        caption.style.display = i === currentIndex ? 'block' : 'none';
    });

}

showSlide(0)
let currentSlide = 0;
const slidesItem = document.querySelector('.slides');
const prevBtn = document.querySelector('#prev');
const nextBtn = document.querySelector('#next');

const totalSlidesItem = document.querySelectorAll('.slide').length;

let currentIndex = 0;

// Function to go to a specific slide
function goToSlideItem(index) {
    currentIndex = index;
    const offset = -100 * currentIndex; // Each slide takes 100% width
    slidesItem.style.transform = `translateX(${offset}%)`;
}

// Event Listeners
prevBtn.addEventListener('click', () => {
    currentIndex = (currentIndex - 1 + totalSlidesItem) % totalSlidesItem;
    goToSlideItem(currentIndex);
});

nextBtn.addEventListener('click', () => {
    console.log(currentIndex)
    currentIndex = (currentIndex + 1) % totalSlidesItem;
    goToSlideItem(currentIndex);
});



const viewMoreIcon = document.getElementById('ViewIcon');
const collapseContent = document.getElementById('collapseLinkExample');




const slides = document.querySelectorAll('.my-carousel-item');
const totalSlides = slides.length;

function moveSlide(direction) {
    currentSlide += direction;
    if (currentSlide < 0) {
        currentSlide = totalSlides - 1;
    } else if (currentSlide >= totalSlides) {
        currentSlide = 0;
    }
    updateCarousel();
}

function updateCarousel() {
    const carousel = document.querySelector('.my-carousel');
    carousel.style.transform = `translateX(-${currentSlide * 100}%)`;
    updateThumbnails();
}

function goToSlide(index) {
    currentSlide = index;
    updateCarousel();
}

function updateThumbnails() {
    const thumbnails = document.querySelectorAll('.my-thumbnails img');
    thumbnails.forEach((thumb, index) => {
        if (index === currentSlide) {
            thumb.classList.add('selected');
        } else {
            thumb.classList.remove('selected');
        }
    });
}

document.addEventListener('DOMContentLoaded', () => {
    const stars = document.querySelectorAll('.rating-container .fa-star');
    let selectedRating = 0;
    stars.forEach(star => {
        star.addEventListener('mouseenter', (e) => {
            const ratingValue = parseInt(e.target.dataset.value);
            hightlightStars(ratingValue)
        });

        star.addEventListener('mouseleave', () => {
            hightlightStars(selectedRating);
        })
        star.addEventListener('click', (e) => {
            selectedRating = parseInt(e.target.dataset.value);
            document.getElementById('ViewRating').value = selectedRating;
            hightlightStars(selectedRating);
        })
    });

    function hightlightStars(ratingValue) {
        stars.forEach(star => {
            if (parseInt(star.dataset.value) <= ratingValue) {
                star.classList.add('hovered');
            } else {
                star.classList.remove('hovered')
            }
        })
    }
})


const openButton = document.querySelectorAll('#openModal');

const modalBackground = document.querySelector('.custombox-overlay');
const modalMap = document.querySelector('.custombox-content');
const modal = document.getElementById('ontargetModal');
const closeButton = document.querySelector(".fa-xmark");
document.querySelectorAll('#openModal').forEach((element) => {
    element.addEventListener('click', async (e) => {
        const idRoom=element.getAttribute('data-idhotel');
        const price=element.getAttribute('data-price');
        const type=element.getAttribute('data-type');
        document.getElementById('pricePopup').textContent='£'+price;
        document.getElementById('typeRoom').textContent=type;
        e.preventDefault(); // Ngăn chặn hành vi mặc định
     await  openModal(idRoom);

    });
});
document.getElementById('closeBtn').addEventListener('click',function (){
    modalMap.style.setProperty('display', 'none', 'important');
    modalMap.style.overflow='hidden'
    modalMap.style.zIndex='0';
    modalMap.style.opacity='0'
    modalBackground.style.setProperty('display', 'none', 'important');
    modalBackground.style.zIndex='0'
    modal.style.setProperty('display', 'none', 'important');
    modal.classList.remove("fade-in");
    modal.classList.add("fade-out");
})
async  function openModal(id) {
    const response=await fetch(`http://localhost:8686/api/Amenity/${id}`);
    if(!response.ok)throw new Error("Failed to fetch amenities");
    const amenities = await response.json();
    renderAmenities(amenities)
    await loadSlides(id)
    console.log(amenities)
    modalMap.style.setProperty('display', 'flex', 'important');
    modalMap.style.overflow='hidden'
    modalMap.style.zIndex='1000';
    modalMap.style.opacity='1'
    modalBackground.style.setProperty('display', 'block', 'important');
    modalBackground.style.zIndex='1000'
    modal.style.setProperty('display', 'block', 'important');
    modal.classList.remove("fade-out");
    modal.classList.add("fade-in");
}
function renderAmenities(amenities){
    const amenitiesList=document.querySelector('#Amenities');
    amenitiesList.innerHTML='';
    amenities.forEach(amenity=>{
        const li=document.createElement('li');
        li.classList.add('mb-3');
        li.innerHTML = `
            <i class="fa-solid fa-wifi" style="margin-right: 1rem;color: #297cbb !important;font-size: 1.5rem"></i>
            ${amenity.name}
        `;
        amenitiesList.appendChild(li);
    })
}
async function loadSlides(roomId){
    try {
    const response=await fetch(`http://localhost:8686/api/room/picture/${roomId}`)
        if (!response.ok) throw new Error("Failed to fetch pictures");

        const pictures = await response.json();
        console.log(pictures)
        renderSlides(pictures)
    }catch (error){
        console.error("Error loading slides:", error);
    }
}
function renderSlides(pictures){
    const slidesContainer = document.getElementById('slides');
    const dotsContainer = document.querySelector('.dots');
    slidesContainer.innerHTML = '';
    dotsContainer.innerHTML = '';
    pictures.forEach((picture, index) => {
        const slide = document.createElement('div');
        slide.classList.add('slide');

        slide.setAttribute('id', 'Slide'); // Thêm id="Slide"

        slide.innerHTML = `
            <img src="http://localhost:8686/images/rooms/${picture.imageUrl}" alt="Slide ${index + 1}">
            <div class="caption">
                <span>Swimming Pool</span>
            </div>
        `;

        slidesContainer.appendChild(slide);
        const dot = document.createElement('span');
        dot.classList.add('dot');
        dot.setAttribute('onclick', `currentSlideIndex(${index})`);
        dotsContainer.appendChild(dot);
    });
}
function closeModal() {

    modal.classList.remove("fade-in");
    modal.classList.add("fade-out");
    setTimeout(() => {
        modalMap.style.setProperty('display', 'none', 'important');
        modalBackground.style.setProperty('display', 'none', 'important');
        modal.style.setProperty('display', 'none', 'important');
    })
}


closeButton.addEventListener('click', closeModal);