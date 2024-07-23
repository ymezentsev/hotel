package com.robot.hotel.user;

public class EmailUtil {

    public static String buildEmailTokenConfirmationMessage(String name, String link) {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" lang=\"en\">\n" +
                "<head>\n" +
                "    <title></title>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "    <!--[if !mso]><!-->\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                "    <!--<![endif]-->\n" +
                "    <meta name=\"x-apple-disable-message-reformatting\" content=\"\" />\n" +
                "    <meta content=\"target-densitydpi=device-dpi\" name=\"viewport\" />\n" +
                "    <meta content=\"true\" name=\"HandheldFriendly\" />\n" +
                "    <meta content=\"width=device-width\" name=\"viewport\" />\n" +
                "    <meta name=\"format-detection\" content=\"telephone=no, date=no, address=no, email=no, url=no\" />\n" +
                "    <style type=\"text/css\">\n" +
                "        table {\n" +
                "            border-collapse: separate;\n" +
                "            table-layout: fixed;\n" +
                "            mso-table-lspace: 0pt;\n" +
                "            mso-table-rspace: 0pt\n" +
                "        }\n" +
                "        table td {\n" +
                "            border-collapse: collapse\n" +
                "        }\n" +
                "        .ExternalClass {\n" +
                "            width: 100%\n" +
                "        }\n" +
                "        .ExternalClass,\n" +
                "        .ExternalClass p,\n" +
                "        .ExternalClass span,\n" +
                "        .ExternalClass font,\n" +
                "        .ExternalClass td,\n" +
                "        .ExternalClass div {\n" +
                "            line-height: 100%\n" +
                "        }\n" +
                "        body, a, li, p, h1, h2, h3 {\n" +
                "            -ms-text-size-adjust: 100%;\n" +
                "            -webkit-text-size-adjust: 100%;\n" +
                "        }\n" +
                "        html {\n" +
                "            -webkit-text-size-adjust: none !important\n" +
                "        }\n" +
                "        body, #innerTable {\n" +
                "            -webkit-font-smoothing: antialiased;\n" +
                "            -moz-osx-font-smoothing: grayscale\n" +
                "        }\n" +
                "        #innerTable img+div {\n" +
                "            display: none;\n" +
                "            display: none !important\n" +
                "        }\n" +
                "        img {\n" +
                "            Margin: 0;\n" +
                "            padding: 0;\n" +
                "            -ms-interpolation-mode: bicubic\n" +
                "        }\n" +
                "        h1, h2, h3, p, a {\n" +
                "            line-height: 1;\n" +
                "            overflow-wrap: normal;\n" +
                "            white-space: normal;\n" +
                "            word-break: break-word\n" +
                "        }\n" +
                "        a {\n" +
                "            text-decoration: none\n" +
                "        }\n" +
                "        h1, h2, h3, p {\n" +
                "            min-width: 100%!important;\n" +
                "            width: 100%!important;\n" +
                "            max-width: 100%!important;\n" +
                "            display: inline-block!important;\n" +
                "            border: 0;\n" +
                "            padding: 0;\n" +
                "            margin: 0\n" +
                "        }\n" +
                "        a[x-apple-data-detectors] {\n" +
                "            color: inherit !important;\n" +
                "            text-decoration: none !important;\n" +
                "            font-size: inherit !important;\n" +
                "            font-family: inherit !important;\n" +
                "            font-weight: inherit !important;\n" +
                "            line-height: inherit !important\n" +
                "        }\n" +
                "        a[href^=\"mailto\"],\n" +
                "        a[href^=\"tel\"],\n" +
                "        a[href^=\"sms\"] {\n" +
                "            color: inherit;\n" +
                "            text-decoration: none\n" +
                "        }\n" +
                "        img,p{margin:0;Margin:0;font-family:Inter Tight,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:21px;font-weight:500;font-style:normal;font-size:16px;text-decoration:none;text-transform:none;letter-spacing:0;direction:ltr;color:#19227d;text-align:center;mso-line-height-rule:exactly;mso-text-raise:2px}h1{margin:0;Margin:0;font-family:Roboto,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:34px;font-weight:400;font-style:normal;font-size:28px;text-decoration:none;text-transform:none;letter-spacing:0;direction:ltr;color:#333;text-align:left;mso-line-height-rule:exactly;mso-text-raise:2px}h2{margin:0;Margin:0;font-family:Lato,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:30px;font-weight:400;font-style:normal;font-size:24px;text-decoration:none;text-transform:none;letter-spacing:0;direction:ltr;color:#333;text-align:left;mso-line-height-rule:exactly;mso-text-raise:2px}h3{margin:0;Margin:0;font-family:Lato,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:26px;font-weight:400;font-style:normal;font-size:20px;text-decoration:none;text-transform:none;letter-spacing:0;direction:ltr;color:#333;text-align:left;mso-line-height-rule:exactly;mso-text-raise:2px}\n" +
                "    </style>\n" +
                "    <style type=\"text/css\">\n" +
                "        @media (min-width: 481px) {\n" +
                "            .hd { display: none!important }\n" +
                "        }\n" +
                "    </style>\n" +
                "    <style type=\"text/css\">\n" +
                "        @media (max-width: 480px) {\n" +
                "            .hm { display: none!important }\n" +
                "        }\n" +
                "    </style>\n" +
                "    <style type=\"text/css\">\n" +
                "        [style*=\"Inter Tight\"] {font-family: 'Inter Tight', BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif !important;}\n" +
                "        @media only screen and (min-width: 481px) {img,p{margin:0;Margin:0;font-family:Inter Tight,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:21px;font-weight:500;font-style:normal;font-size:16px;text-decoration:none;text-transform:none;letter-spacing:0;direction:ltr;color:#19227d;text-align:center;mso-line-height-rule:exactly;mso-text-raise:2px}h1{margin:0;Margin:0;font-family:Roboto,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:34px;font-weight:400;font-style:normal;font-size:28px;text-decoration:none;text-transform:none;letter-spacing:0;direction:ltr;color:#333;text-align:left;mso-line-height-rule:exactly;mso-text-raise:2px}h2{margin:0;Margin:0;font-family:Lato,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:30px;font-weight:400;font-style:normal;font-size:24px;text-decoration:none;text-transform:none;letter-spacing:0;direction:ltr;color:#333;text-align:left;mso-line-height-rule:exactly;mso-text-raise:2px}h3{margin:0;Margin:0;font-family:Lato,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:26px;font-weight:400;font-style:normal;font-size:20px;text-decoration:none;text-transform:none;letter-spacing:0;direction:ltr;color:#333;text-align:left;mso-line-height-rule:exactly;mso-text-raise:2px}.t15{width:750px!important}.t30{width:720px!important}.t34{width:600px!important}.t40{line-height:41px!important;font-size:35px!important}.t44,.t66{width:600px!important}}\n" +
                "    </style>\n" +
                "    <style type=\"text/css\" media=\"screen and (min-width:481px)\">.moz-text-html img,.moz-text-html p{margin:0;Margin:0;font-family:Inter Tight,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:21px;font-weight:500;font-style:normal;font-size:16px;text-decoration:none;text-transform:none;letter-spacing:0;direction:ltr;color:#19227d;text-align:center;mso-line-height-rule:exactly;mso-text-raise:2px}.moz-text-html h1{margin:0;Margin:0;font-family:Roboto,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:34px;font-weight:400;font-style:normal;font-size:28px;text-decoration:none;text-transform:none;letter-spacing:0;direction:ltr;color:#333;text-align:left;mso-line-height-rule:exactly;mso-text-raise:2px}.moz-text-html h2{margin:0;Margin:0;font-family:Lato,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:30px;font-weight:400;font-style:normal;font-size:24px;text-decoration:none;text-transform:none;letter-spacing:0;direction:ltr;color:#333;text-align:left;mso-line-height-rule:exactly;mso-text-raise:2px}.moz-text-html h3{margin:0;Margin:0;font-family:Lato,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:26px;font-weight:400;font-style:normal;font-size:20px;text-decoration:none;text-transform:none;letter-spacing:0;direction:ltr;color:#333;text-align:left;mso-line-height-rule:exactly;mso-text-raise:2px}.moz-text-html .t15{width:750px!important}.moz-text-html .t30{width:720px!important}.moz-text-html .t34{width:600px!important}.moz-text-html .t40{line-height:41px!important;font-size:35px!important}.moz-text-html .t44,.moz-text-html .t66{width:600px!important}</style>\n" +
                "    <!--[if !mso]><!-->\n" +
                "    <link href=\"https://fonts.googleapis.com/css2?family=Inter+Tight:wght@500;600;700;900&amp;display=swap\" rel=\"stylesheet\" type=\"text/css\" />\n" +
                "    <!--<![endif]-->\n" +
                "    <!--[if mso]>\n" +
                "    <style type=\"text/css\">\n" +
                "        img,p{margin:0;Margin:0;font-family:Inter Tight,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:21px;font-weight:500;font-style:normal;font-size:16px;text-decoration:none;text-transform:none;letter-spacing:0;direction:ltr;color:#19227d;text-align:center;mso-line-height-rule:exactly;mso-text-raise:2px}h1{margin:0;Margin:0;font-family:Roboto,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:34px;font-weight:400;font-style:normal;font-size:28px;text-decoration:none;text-transform:none;letter-spacing:0;direction:ltr;color:#333;text-align:left;mso-line-height-rule:exactly;mso-text-raise:2px}h2{margin:0;Margin:0;font-family:Lato,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:30px;font-weight:400;font-style:normal;font-size:24px;text-decoration:none;text-transform:none;letter-spacing:0;direction:ltr;color:#333;text-align:left;mso-line-height-rule:exactly;mso-text-raise:2px}h3{margin:0;Margin:0;font-family:Lato,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif;line-height:26px;font-weight:400;font-style:normal;font-size:20px;text-decoration:none;text-transform:none;letter-spacing:0;direction:ltr;color:#333;text-align:left;mso-line-height-rule:exactly;mso-text-raise:2px}td.t15,td.t30{width:800px !important}td.t34{width:600px !important}h1.t40{line-height:41px !important;font-size:35px !important}td.t44,td.t66{width:600px !important}\n" +
                "    </style>\n" +
                "    <![endif]-->\n" +
                "    <!--[if mso]>\n" +
                "    <xml>\n" +
                "        <o:OfficeDocumentSettings>\n" +
                "            <o:AllowPNG/>\n" +
                "            <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                "        </o:OfficeDocumentSettings>\n" +
                "    </xml>\n" +
                "    <![endif]-->\n" +
                "</head>\n" +
                "<body class=\"t0\" style=\"min-width:100%;Margin:0px;padding:0px;background-color:#292929;\"><div class=\"t1\" style=\"background-color:#292929;\"><table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\"><tr><td class=\"t2\" style=\"font-size:0;line-height:0;mso-line-height-rule:exactly;background-color:#292929;\" valign=\"top\" align=\"center\">\n" +
                "    <!--[if mso]>\n" +
                "    <v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"true\" stroke=\"false\">\n" +
                "        <v:fill color=\"#292929\"/>\n" +
                "    </v:background>\n" +
                "    <![endif]-->\n" +
                "    <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\" id=\"innerTable\"><tr><td><div class=\"t3\" style=\"mso-line-height-rule:exactly;mso-line-height-alt:60px;line-height:60px;font-size:1px;display:block;\">&nbsp;</div></td></tr><tr><td>\n" +
                "        <table class=\"t10\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\"><tr>\n" +
                "            <!--[if !mso]><!--><td class=\"t11\" style=\"width:400px;padding:0 20px 0 20px;\">\n" +
                "            <!--<![endif]-->\n" +
                "            <!--[if mso]><td class=\"t11\" style=\"width:440px;padding:0 20px 0 20px;\"><![endif]-->\n" +
                "            <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\"><tr><td>\n" +
                "                <table class=\"t14\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\"><tr>\n" +
                "                    <!--[if !mso]><!--><td class=\"t15\" style=\"background-color:#CDD6B0;overflow:hidden;width:430px;padding:26px 25px 26px 25px;border-radius:14px 14px 0 0;\">\n" +
                "                    <!--<![endif]-->\n" +
                "                    <!--[if mso]><td class=\"t15\" style=\"background-color:#CDD6B0;overflow:hidden;width:480px;padding:26px 25px 26px 25px;border-radius:14px 14px 0 0;\"><![endif]-->\n" +
                "                    <div class=\"t21\" style=\"display:inline-table;width:100%;text-align:center;vertical-align:top;\">\n" +
                "                        <!--[if mso]>\n" +
                "                        <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" valign=\"top\" width=\"55\"><tr><td width=\"55\" valign=\"top\"><![endif]-->\n" +
                "                        <div class=\"t89\" style=\"display:inline-table;text-align:initial;vertical-align:inherit;width:100%;max-width:55px;\">\n" +
                "                            <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" class=\"t91\"><tr>\n" +
                "                                <td class=\"t92\"><div style=\"font-size:0px;\"><img class=\"t93\" style=\"display:block;border:0;height:auto;width:100%;Margin:0;max-width:100%;\" width=\"55\" height=\"36.4375\" alt=\"\" src=\"https://5b00d4a8-bf2e-4a36-b0d7-96136dd8cb6e.b-cdn.net/e/2c0e8a38-1a94-46c9-9a43-c0ef2d0fbd0e/a0bfa400-6632-45e5-9b6b-ce8b360fb0de.png\"/></div></td>\n" +
                "                            </tr></table>\n" +
                "                        </div>\n" +
                "                        <!--[if mso]>\n" +
                "                        </td>\n" +
                "                        </tr></table>\n" +
                "                        <![endif]-->\n" +
                "                    </div></td>\n" +
                "                </tr></table>\n" +
                "            </td></tr><tr><td>\n" +
                "                <table class=\"t29\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\"><tr>\n" +
                "                    <!--[if !mso]><!--><td class=\"t30\" style=\"background-color:#FFFFFF;overflow:hidden;width:400px;padding:40px 40px 40px 40px;border-radius:0 0 14px 14px;\">\n" +
                "                    <!--<![endif]-->\n" +
                "                    <!--[if mso]><td class=\"t30\" style=\"background-color:#FFFFFF;overflow:hidden;width:480px;padding:40px 40px 40px 40px;border-radius:0 0 14px 14px;\"><![endif]-->\n" +
                "                    <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\"><tr><td>\n" +
                "                        <table class=\"t33\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\"><tr>\n" +
                "                            <!--[if !mso]><!--><td class=\"t34\" style=\"width:480px;\">\n" +
                "                            <!--<![endif]-->\n" +
                "                            <!--[if mso]><td class=\"t34\" style=\"width:480px;\"><![endif]-->\n" +
                "                            <h1 class=\"t40\" style=\"margin:0;Margin:0;font-family:BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif,'Inter Tight';line-height:35px;font-weight:900;font-style:normal;font-size:30px;text-decoration:none;text-transform:none;direction:ltr;color:#121212;text-align:center;mso-line-height-rule:exactly;mso-text-raise:2px;\">Hello, " + name + "</h1></td>\n" +
                "                        </tr></table>\n" +
                "                    </td></tr><tr><td><div class=\"t41\" style=\"mso-line-height-rule:exactly;mso-line-height-alt:20px;line-height:20px;font-size:1px;display:block;\">&nbsp;</div></td></tr><tr><td>\n" +
                "                        <table class=\"t43\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\"><tr>\n" +
                "                            <!--[if !mso]><!--><td class=\"t44\" style=\"width:480px;\">\n" +
                "                            <!--<![endif]-->\n" +
                "                            <!--[if mso]><td class=\"t44\" style=\"width:480px;\"><![endif]-->\n" +
                "                            <p class=\"t50\" style=\"margin:0;Margin:0;font-family:BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif,'Inter Tight';line-height:21px;font-weight:500;font-style:normal;font-size:16px;text-decoration:none;text-transform:none;direction:ltr;color:#111111;text-align:center;mso-line-height-rule:exactly;mso-text-raise:2px;\">Thank you for registering on our website! To complete the registration process and activate your account, please verify your email address by clicking the following link</p></td>\n" +
                "                        </tr></table>\n" +
                "                    </td></tr><tr><td><div class=\"t51\" style=\"mso-line-height-rule:exactly;mso-line-height-alt:20px;line-height:20px;font-size:1px;display:block;\">&nbsp;</div></td></tr><tr><td>\n" +
                "                        <table class=\"t53\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\"><tr>\n" +
                "                            <!--[if !mso]><!--><td class=\"t54\" style=\"background-color:#CDD6B0;overflow:hidden;width:286px;text-align:center;line-height:40px;mso-line-height-rule:exactly;mso-text-raise:8px;border-radius:12px 12px 12px 12px;\">\n" +
                "                            <!--<![endif]-->\n" +
                "                            <!--[if mso]><td class=\"t54\" style=\"background-color:#CDD6B0;overflow:hidden;width:286px;text-align:center;line-height:40px;mso-line-height-rule:exactly;mso-text-raise:8px;border-radius:12px 12px 12px 12px;\"><![endif]-->\n" +
                "                            <a href=\"" + link + "\" class=\"t60\" style=\"display:block;margin:0;Margin:0;font-family:BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif,'Inter Tight';line-height:40px;font-weight:600;font-style:normal;font-size:14px;text-decoration:none;direction:ltr;color:#292929;text-align:center;mso-line-height-rule:exactly;mso-text-raise:8px;\">Verify Email</a></td>\n" +
                "                        </tr></table>\n" +
                "                    </td></tr><tr><td><div class=\"t63\" style=\"mso-line-height-rule:exactly;mso-line-height-alt:20px;line-height:20px;font-size:1px;display:block;\">&nbsp;</div></td></tr><tr><td>\n" +
                "                        <table class=\"t65\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\"><tr>\n" +
                "                            <!--[if !mso]><!--><td class=\"t66\" style=\"width:480px;\">\n" +
                "                            <!--<![endif]-->\n" +
                "                            <!--[if mso]><td class=\"t66\" style=\"width:480px;\"><![endif]-->\n" +
                "                            <p class=\"t72\" style=\"margin:0;Margin:0;font-family:BlinkMacSystemFont,Segoe UI,Helvetica Neue,Arial,sans-serif,'Inter Tight';line-height:21px;font-weight:500;font-style:normal;font-size:13px;text-decoration:none;text-transform:none;direction:ltr;color:#121212;text-align:center;mso-line-height-rule:exactly;mso-text-raise:2px;\">Didn&#39;t request this? <a class=\"t73\" href=\"https://tabular.email\" style=\"margin:0;Margin:0;font-weight:700;font-style:normal;text-decoration:none;direction:ltr;color:#121212;mso-line-height-rule:exactly;\" target=\"_blank\">Let us know</a>.</p></td>\n" +
                "                        </tr></table>\n" +
                "                    </td></tr></table></td>\n" +
                "                </tr></table>\n" +
                "        </tr></table>\n" +
                "    </td></tr><tr><td><div class=\"t4\" style=\"mso-line-height-rule:exactly;mso-line-height-alt:60px;line-height:60px;font-size:1px;display:block;\">&nbsp;</div></td></tr></table></td></tr></table></div></body>\n" +
                "</html>";
    }

    public static String buildForgotPasswordTokenConfirmationMessage(String name, String link) {
        return "<!doctype html>\n" +
                "<html lang=\"en-US\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\" />\n" +
                "    <title>Reset Password Email Template</title>\n" +
                "    <meta name=\"description\" content=\"\">\n" +
                "    <style type=\"text/css\">\n" +
                "        a:hover {text-decoration: underline !important;}\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body marginheight=\"0\" topmargin=\"0\" marginwidth=\"0\" style=\"margin: 0px; background-color: #f2f3f8;\" leftmargin=\"0\">\n" +
                "    <!--100% body table-->\n" +
                "    <table cellspacing=\"0\" border=\"0\" cellpadding=\"0\" width=\"100%\" bgcolor=\"#f2f3f8\"\n" +
                "        style=\"@import url(https://fonts.googleapis.com/css?family=Rubik:300,400,500,700|Open+Sans:300,400,600,700); font-family: 'Open Sans', sans-serif;\">\n" +
                "        <tr>\n" +
                "            <td>\n" +
                "                <table style=\"background-color: #f2f3f8; max-width:670px;  margin:0 auto;\" width=\"100%\" border=\"0\"\n" +
                "                    align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                    <tr>\n" +
                "                        <td style=\"height:80px;\">&nbsp;</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td style=\"text-align:center;\">\n" +
                "                          <a href=\"https://heart-of-ukraine.com\" title=\"logo\" target=\"_blank\">\n" +
                "                            <img width=\"60\" src=\"https://i.ibb.co/hL4XZp2/android-chrome-192x192.png\" title=\"logo\" alt=\"logo\">\n" +
                "                          </a>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td style=\"height:20px;\">&nbsp;</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td>\n" +
                "                            <table width=\"95%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "                                style=\"max-width:670px;background:#fff; border-radius:3px; text-align:center;-webkit-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);-moz-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);box-shadow:0 6px 18px 0 rgba(0,0,0,.06);\">\n" +
                "                                <tr>\n" +
                "                                    <td style=\"height:40px;\">&nbsp;</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"padding:0 35px;\">\n" +
                "                                        <h1 style=\"color:#1e1e2d; font-weight:500; margin:0;font-size:32px;font-family:'Rubik',sans-serif;\">Hey, " + name + "! You have\n" +
                "                                            requested to reset your password</h1>\n" +
                "                                        <span\n" +
                "                                            style=\"display:inline-block; vertical-align:middle; margin:29px 0 26px; border-bottom:1px solid #cecece; width:100px;\"></span>\n" +
                "                                        <p style=\"color:#455056; font-size:15px;line-height:24px; margin:0;\">\n" +
                "                                            We cannot simply send you your old password. A unique link to reset your\n" +
                "                                            password has been generated for you. To reset your password, click the\n" +
                "                                            following link and follow the instructions.\n" +
                "                                        </p>\n" +
                "                                        <a href=\"" + link + "\"\n" +
                "                                            style=\"background:#20e277;text-decoration:none !important; font-weight:500; margin-top:35px; color:#fff;text-transform:uppercase; font-size:14px;padding:10px 24px;display:inline-block;border-radius:50px;\">Reset\n" +
                "                                            Password</a>\n" +
                "                                    </td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"height:40px;\">&nbsp;</td>\n" +
                "                                </tr>\n" +
                "                            </table>\n" +
                "                        </td>\n" +
                "                    <tr>\n" +
                "                        <td style=\"height:20px;\">&nbsp;</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td style=\"text-align:center;\">\n" +
                "                            <p style=\"font-size:14px; color:rgba(69, 80, 86, 0.7411764705882353); line-height:18px; margin:0 0 0;\">&copy; <strong>www.heart-of-ukraine.com</strong></p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td style=\"height:80px;\">&nbsp;</td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "    <!--/100% body table-->\n" +
                "</body>\n" +
                "\n" +
                "</html>";
    }
}
