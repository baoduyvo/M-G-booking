package com.vtnq.web.Service;

import com.vtnq.web.DTOs.ContractOwner.ContractOwnerDto;
import com.vtnq.web.Entities.ContractOwner;
import com.vtnq.web.Repositories.ContractOwnerRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractOwnerServiceImplement implements ContractOwnerService {
    @Autowired
    private ContractOwnerRepository contractOwnerRepository;
    @Autowired
    private JavaMailSenderImpl mailSender;

    @Override
    public List<ContractOwnerDto> findAll(int id) {
        try {
            return contractOwnerRepository.FindAll(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void SendConfirmationEmail(String email, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            // Sử dụng MimeMessageHelper để hỗ trợ HTML
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("My Contract");

            // Nội dung email định dạng HTML
            helper.setText(htmlContent, true); // 'true' cho phép gửi HTML

            // Gửi email
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean AcceptRegister(ContractOwnerDto contractOwnerDto) {
        try {
            ContractOwner contractOwner=contractOwnerRepository.findById(contractOwnerDto.getId())
                    .orElseThrow(() -> new Exception("Contract Owner not found"));
                contractOwner.setStatus(true);
                contractOwnerRepository.save(contractOwner);
            String message="<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Chữ Ký Điện Tử</title>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: 'Arial', sans-serif;\n" +
                    "            margin: 0;\n" +
                    "            padding: 0;\n" +
                    "            background-color: #f7f7f7;\n" +
                    "            color: #333;\n" +
                    "        }\n" +
                    "\n" +
                    "        .container {\n" +
                    "            width: 80%;\n" +
                    "            margin: 20px auto;\n" +
                    "            padding: 20px;\n" +
                    "            background-color: white;\n" +
                    "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\n" +
                    "            border-radius: 10px;\n" +
                    "        }\n" +
                    "\n" +
                    "        h1 {\n" +
                    "            text-align: center;\n" +
                    "            font-size: 24px;\n" +
                    "            color: #4CAF50;\n" +
                    "        }\n" +
                    "\n" +
                    "        h2, h3 {\n" +
                    "            color: #333;\n" +
                    "        }\n" +
                    "\n" +
                    "        p {\n" +
                    "            line-height: 1.6;\n" +
                    "            margin-bottom: 10px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .contract-section {\n" +
                    "            margin-bottom: 20px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .contract-section p {\n" +
                    "            margin-left: 20px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .signature-section {\n" +
                    "            margin-top: 40px;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "\n" +
                    "        .signature-title {\n" +
                    "            font-size: 20px;\n" +
                    "            font-weight: bold;\n" +
                    "            margin-bottom: 20px;\n" +
                    "        }\n" +
                    "\n" +
                    "        .button-container {\n" +
                    "            margin-top: 20px;\n" +
                    "            text-align: center;\n" +
                    "        }\n" +
                    "\n" +
                    "        .button-container a {\n" +
                    "            padding: 10px 20px;\n" +
                    "            background-color: #4CAF50;\n" +
                    "            color: white;\n" +
                    "            border: none;\n" +
                    "            border-radius: 5px;\n" +
                    "            cursor: pointer;\n" +
                    "            font-size: 16px;\n" +
                    "            margin: 5px;\n" +
                    "            transition: background-color 0.3s;\n" +
                    "        }\n" +
                    "\n" +
                    "        .button-container a:hover {\n" +
                    "            background-color: #45a049;\n" +
                    "        }\n" +
                    "\n" +
                    "        .footer {\n" +
                    "            text-align: center;\n" +
                    "            margin-top: 40px;\n" +
                    "            font-size: 14px;\n" +
                    "            color: #777;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body> \n" +
                    "<div class=\"container\">\n" +
                    "    <h1>HỢP ĐỒNG ĐĂNG KÝ KHÁCH SẠN</h1>\n" +
                    "\n" +
                    "    <!-- Contract Information -->\n" +
                    "\n" +
                    "    <div class=\"contract-section\">\n" +
                    "        <h2>KHÁCH HÀNG</h2>\n" +
                    "        <p><strong>Tên khách hàng:</strong> " +contractOwnerDto.getOwnerName()+"</p>\n"+
                    "        <p><strong>Địa chỉ:</strong> " +contractOwnerDto.getAddress()+"</p>\n"+
                    "        <p><strong>Số điện thoại:</strong> " +contractOwnerDto.getPhone()+"</p>\n"+
                    "         <p><strong>Email:</strong> " +contractOwnerDto.getEmail()+"</p>\n"+
                    "    </div>\n" +
                    "\n" +
                    "\n" +
                    "    <div class=\"contract-section\">\n" +
                    "        <h3>Điều Khoản Hợp Đồng</h3>\n" +
                    "        <p><strong>1. Điều kiện thanh toán:</strong> Khách hàng phải thanh toán 50% số tiền hợp đồng trước khi ký kết, và phần còn lại phải thanh toán đầy đủ trước ngày 15/12/2024. Thanh toán có thể được thực hiện qua chuyển khoản ngân hàng hoặc thẻ tín dụng.</p>\n" +
                    "        <p><strong>2. Điều kiện hủy hợp đồng:</strong> Khách hàng có thể hủy hợp đồng miễn phí nếu thông báo ít nhất 7 ngày trước ngày bắt đầu hợp đồng. Nếu hủy sau thời gian này, khách hàng sẽ phải chịu phí hủy hợp đồng là 30% giá trị hợp đồng</p>\n" +
                    "        <p><strong>3. Điều kiện vi phạm hợp đồng:</strong> Nếu bất kỳ bên nào vi phạm các điều khoản hợp đồng, bên bị vi phạm có quyền đơn phương chấm dứt hợp đồng mà không cần thông báo trước. Phí vi phạm sẽ được tính theo tỷ lệ phần trăm của tổng giá trị hợp đồng.</p>\n" +
                    "        <p><strong>4. Điều kiện bảo mật thông tin:</strong> Các bên cam kết bảo mật mọi thông tin liên quan đến hợp đồng, bao gồm thông tin cá nhân và tài chính. Mọi hành vi tiết lộ thông tin mà không có sự đồng ý của các bên sẽ bị xử lý theo quy định của pháp luật.</p>\n" +
                    "        <p><strong>5. Điều kiện về quyền sở hữu tài sản:</strong> Tất cả các tài sản vật chất hoặc kỹ thuật liên quan đến hợp đồng, bao gồm thiết bị và phần mềm, sẽ thuộc quyền sở hữu của khách sạn hoặc bên cung cấp dịch vụ cho đến khi hợp đồng kết thúc và tất cả nghĩa vụ thanh toán đã được thực hiện đầy đủ.</p>\n" +
                    "        <p><strong>6. Điều kiện giải quyết tranh chấp:</strong> Mọi tranh chấp phát sinh từ hợp đồng sẽ được giải quyết thông qua thương lượng giữa các bên. Nếu không thể đạt được thỏa thuận, tranh chấp sẽ được giải quyết tại tòa án có thẩm quyền tại địa phương của bên khởi kiện.</p>\n" +
                    "        <p><strong>7. Điều kiện thay đổi hợp đồng:</strong> Bất kỳ thay đổi nào đối với hợp đồng này phải được thỏa thuận bằng văn bản và được ký kết bởi cả hai bên. Các thay đổi sẽ có hiệu lực kể từ ngày ký.</p>\n" +
                    "    </div>\n" +
                    "\n" +
                    "    <!-- Button to sign -->\n" +
                    "\n" +
                    "    <div class=\"footer\">\n" +
                    "        <p>© 2024 [Tên Công Ty] - Tất cả quyền lợi được bảo vệ.</p>\n" +
                    "    </div>\n" +
                    "</div>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>\n";
            SendConfirmationEmail("tranp6648@gmail.com", message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
