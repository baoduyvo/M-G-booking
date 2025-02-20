const ctx = document.getElementById('radialBarChart').getContext('2d');

// Lấy giá trị từ input
let cityInput = document.getElementById('city').value;
let cityValue = parseFloat(cityInput); // Chuyển thành số
const AirportInput=document.getElementById('Airport').value;
const FlightInput=document.getElementById('Flight').value;
const bookingInput=document.getElementById('booking').value;

// Dữ liệu
const data = {
    labels: ['City', 'Airport', 'Flight', 'booking'],
    datasets: [{
        label: 'Sales',
        data: [cityValue,AirportInput, FlightInput, bookingInput], // Giá trị mỗi phần
        backgroundColor: [
            'rgba(255, 99, 132, 0.7)',
            'rgba(54, 162, 235, 0.7)',
            'rgba(255, 206, 86, 0.7)',
            'rgba(75, 192, 192, 0.7)',
            'rgba(153, 102, 255, 0.7)',
        ],
        hoverBackgroundColor: [
            'rgba(255, 99, 132, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)',
            'rgba(75, 192, 192, 1)',
            'rgba(153, 102, 255, 1)',
        ],
        borderWidth: 0, // Ẩn đường viền
    }]
};

// Cấu hình biểu đồ
const config = {
    type: 'doughnut',
    data: data,
    options: {
        responsive: true,
        cutout: '70%', // Tăng kích thước phần trống để hiển thị giá trị rõ hơn
        plugins: {
            legend: {
                display: true,
                position: 'bottom',
                labels: {
                    font: {
                        size: 14
                    },
                    color: '#444'
                }
            },
            tooltip: {
                enabled: true,
                callbacks: {
                    label: function (context) {
                        return `${context.label}: ${context.raw}`;
                    }
                }
            },
            datalabels: {
                color: '#fff', // Màu chữ giá trị
                font: {
                    size: 16,
                    weight: 'bold'
                },
                formatter: function (value, context) {
                    return value ; // Định dạng giá trị
                },
                anchor: 'end', // Vị trí chữ: Gần cuối thanh
                align: 'start', // Căn chỉnh chữ: Bên trong thanh
                offset: -10, // Đưa chữ vào trong thanh
                borderRadius: 4, // Làm bo góc chữ
                backgroundColor: function (context) {
                    return context.dataset.backgroundColor[context.dataIndex]; // Nền chữ trùng màu với thanh
                },
                padding: 6, // Tăng khoảng cách chữ
            }
        }
    },
    plugins: [ChartDataLabels]
};

// Tạo biểu đồ
const radialBarChart = new Chart(ctx, config);
