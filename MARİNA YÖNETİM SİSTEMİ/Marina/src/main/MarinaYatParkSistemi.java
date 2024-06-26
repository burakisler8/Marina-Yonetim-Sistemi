package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MarinaYatParkSistemi extends JFrame {

    private Connection baglanti;
    private JButton giristusu;
    private JButton cikistusu;
    private JButton listelemetusu;
    private JButton guncellemetusu;
    private JButton programikapat;

    public String veritabanikullaniciadi = "root";
    public String veritabanisifresi = "";

    public MarinaYatParkSistemi() {

        setTitle("Marina Yat Park Sistemi");

        // arkaplan resmini eklemek için aşağıdaki işlemleri yapıyoruz
        try {
            // img klasörü altındaki arkaplan2.png resmini okuyoruz
            BufferedImage img = ImageIO.read(new File("img/arkaplan.jpg"));
            ImageIcon icon = new ImageIcon(img);
            JLabel arkaplan = new JLabel(icon);
            // arkaplan resmini panelin içine ekliyoruz
            setContentPane(arkaplan);
            // bu kod eklenmezse sadece arkaplan resmi görünür. paneldeki tüm itemların görünmesi için flowlayout kullanıyoruz.
            // itemları yazım sırasına göre ekliyor. Böylece önce arkaplan resmi, sonra itemlar eklenebiliyor. itemlar üst üste gelebiliyor.
            setLayout(new FlowLayout());
        } catch (IOException e) {
            e.printStackTrace();
        }

        setSize(1024, 580);
        // panelin boyutunu sabitledik
        setResizable(false);
        // ekranın ortasında açılması için aşağıdaki kodu yazdık
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel panel = new JPanel();

        giristusu = new JButton("Giriş Kaydı");
        giristusu.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        giristusu.setPreferredSize(new Dimension(150, 50));
        // giris btn arkaplan eklmek için aşağıdaki kodu yazdık
        ImageIcon icongiris = new ImageIcon("img/giris.png");
        Image resimgiris = icongiris.getImage();
        // resimi küçültmek için aşağıdaki kodu yazdık
        Image boyutlandirilangirisresim = resimgiris.getScaledInstance(60, 30, Image.SCALE_SMOOTH);
        giristusu.setIcon(new ImageIcon(boyutlandirilangirisresim));

        cikistusu = new JButton("Çıkış Yap");
        cikistusu.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        cikistusu.setPreferredSize(new Dimension(150, 50));
        // cikis btn arkaplan eklmek için aşağıdaki kodu yazdık
        ImageIcon iconcikis = new ImageIcon("img/cikis.png");
        Image resimcikis = iconcikis.getImage();
        Image boyutlandirilancikisresim = resimcikis.getScaledInstance(60, 30, Image.SCALE_SMOOTH);
        cikistusu.setIcon(new ImageIcon(boyutlandirilancikisresim));

        listelemetusu = new JButton("Yatları Listele");
        listelemetusu.setBorder(BorderFactory.createLineBorder(Color.CYAN, 2));
        listelemetusu.setPreferredSize(new Dimension(150, 50));

        guncellemetusu = new JButton("Güncelle");
        guncellemetusu.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        guncellemetusu.setPreferredSize(new Dimension(150, 50));

        programikapat = new JButton("Uygulamayı Kapat");
        programikapat.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
        programikapat.setPreferredSize(new Dimension(150, 50));

        giristusu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yatGirisYap();
            }
        });

        cikistusu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yatCikisYap();
            }
        });

        listelemetusu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yatlariListele();
            }


        });

        guncellemetusu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                yatlariGuncelle();
            }
        });


        programikapat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                programikapat();
            }
        });

        panel.add(giristusu);
        panel.add(cikistusu);
        panel.add(listelemetusu);
        panel.add(guncellemetusu);
        panel.add(programikapat);

        add(panel);

        setVisible(false);

        if (isValidLogin()) {
            setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Hatalı Giriş Sistem Kapatılıyor...");
            System.exit(0);
        }
    }


    private void programikapat() {

        JOptionPane.showMessageDialog(this, "Sistem Kapatılıyor...");

        System.exit(0);

    }


    private void yatlariGuncelle() {

        String ruhsatnum = JOptionPane.showInputDialog(this, "Güncellemek istediğiniz Yat Ruhsat Numarası:");

        if (ruhsatnum == null || ruhsatnum.isEmpty()) {
            return; // Kullanıcı işlemi iptal etti veya boş bir değer girdi
        }

        try {
            // Veritabanında ruhsatnum kontrolü
            baglanti = DriverManager.getConnection("jdbc:mysql://localhost:3306/yatlar", veritabanikullaniciadi, veritabanisifresi);
            PreparedStatement checkStatement = baglanti.prepareStatement("SELECT * FROM yatlar WHERE ruhsatnum = ?");
            checkStatement.setString(1, ruhsatnum);
            ResultSet baglantisonuc = checkStatement.executeQuery();

            if (baglantisonuc.next()) {
                // Belirtilen ruhsatnum'a sahip bir yat bulundu yat için güncellenmiş bilgileri alıyorum
                String uzunluk = JOptionPane.showInputDialog(this, "Yat Uzunluğu:");
                String ad = JOptionPane.showInputDialog(this, "Yat Sahibinin Adı:");
                String telefon = JOptionPane.showInputDialog(this, "Yat Sahibinin Telefonu:");

                if (uzunluk == null || ad == null || telefon == null) {
                    return; // Kullanıcı işlemi iptal etti veya boş bir değer girdi
                }

                // Veritabanında güncelleme işlemini yap
                PreparedStatement updateStatement = baglanti.prepareStatement(
                        "UPDATE yatlar SET uzunluk = ?, ad = ?, telefon = ? WHERE ruhsatnum = ?"
                );
                updateStatement.setString(1, uzunluk);
                updateStatement.setString(2, ad);
                updateStatement.setString(3, telefon);
                updateStatement.setString(4, ruhsatnum);

                int sguncelleme = updateStatement.executeUpdate();

                if (sguncelleme > 0) {
                    // Güncelleme başarılı oldu
                    JOptionPane.showMessageDialog(this, "Yat Güncellendi!\n" +
                            "Yat Ruhsat Numarası: " + ruhsatnum + "\n" +
                            "Yat Uzunluğu: " + uzunluk + "\n" +
                            "Yat Sahibinin Adı: " + ad + "\n" +
                            "Yat Sahibinin Telefonu: " + telefon);
                } else {
                    JOptionPane.showMessageDialog(this, "Yat Güncelleme Başarısız!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Belirtilen ruhsat numarasına sahip bir yat bulunamadı.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Yat güncellenirken bir hata oluştu! Hata Detayı: " + e.getMessage());
        } finally {
            try {
                if (baglanti != null) {
                    baglanti.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private void yatGirisYap() {

        String ruhsatnum = JOptionPane.showInputDialog(this, "Yat Ruhsat numarası:");
        String uzunluk = JOptionPane.showInputDialog(this, "Yat Uzunluğu:");
        String ad = JOptionPane.showInputDialog(this, "Yat Sahibinin adı:");
        String telefon = JOptionPane.showInputDialog(this, "Yat Sahibinin Telefonu:");

        if (ruhsatnum == null || uzunluk == null || ad == null || telefon == null) {
            return; // Kullanıcı pencereyi kapattı veya iptal etti
        }

        if (ruhsatnum.isEmpty() || uzunluk.isEmpty() || ad.isEmpty() || telefon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun!");
        } else {
            try {
                // Veritabanında ruhsatnum ile kayıt var mı kontrolü yapıyorum
                baglanti = DriverManager.getConnection("jdbc:mysql://localhost:3306/yatlar", veritabanikullaniciadi, veritabanisifresi);
                PreparedStatement kontroldata = baglanti.prepareStatement("SELECT * FROM yatlar WHERE ruhsatnum = ?");
                kontroldata.setString(1, ruhsatnum);
                ResultSet sonuckontrol = kontroldata.executeQuery();

                if (sonuckontrol.next()) {
                    JOptionPane.showMessageDialog(this, "Bu numaraya sahip yat zaten kayıtlı.");
                } else {
                    // Yatın kaydedilmediği durumda işleme devam et
                    PreparedStatement yatgirisdata = baglanti.prepareStatement("INSERT INTO yatlar VALUES (?, ?, ?, ?, ?)");
                    yatgirisdata.setString(1, ruhsatnum);
                    yatgirisdata.setString(2, uzunluk);
                    yatgirisdata.setString(3, ad);
                    yatgirisdata.setString(4, telefon);

                    Date girisSaati = new Date();
                    SimpleDateFormat tarihformati = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    yatgirisdata.setString(5, tarihformati.format(girisSaati));

                    yatgirisdata.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Yat Giriş Yaptı!\nYat Ruhsat numarası: " + ruhsatnum +
                            "\nYat uzunlugu: " + uzunluk + "\nYat Sahibinin Adı: " + ad + "\nYat Sahibinin Telefonu: " + telefon +
                            "\nGiriş Saati: " + tarihformati.format(girisSaati));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Yat eklenirken bir hata oluştu! Hata Detayı: " + e.getMessage());
            } finally {
                try {
                    baglanti.close(); // Bağlantıyı kapat
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private boolean isValidLogin() {

        JTextField kullaniciadialani = new JTextField();
        JPasswordField sifrealani = new JPasswordField();

        Object[] message = {
                "Kullanıcı Adı:", kullaniciadialani,
                "Şifre:", sifrealani
        };

        int girissecenek = JOptionPane.showConfirmDialog(this, message, "Giriş", JOptionPane.OK_CANCEL_OPTION);

        if (girissecenek == JOptionPane.OK_OPTION) {
            String kullaniciadi = kullaniciadialani.getText();
            String sifre = new String(sifrealani.getPassword());

            try {
                // kullanıcı tablosunda girilen kullanıcı adı ve şifre ile kayıt var mı kontrolü yapıyorum
                baglanti = DriverManager.getConnection("jdbc:mysql://localhost:3306/yatlar", veritabanikullaniciadi, veritabanisifresi);
                PreparedStatement preparedStatement = baglanti.prepareStatement("SELECT * FROM kullanicilar WHERE kullaniciadi = ? AND sifre = ?");
                preparedStatement.setString(1, kullaniciadi);
                preparedStatement.setString(2, sifre);

                ResultSet sonucset = preparedStatement.executeQuery();

                if (sonucset.next()) {
                    // Giriş başarılı
                    return true;
                } else {
                    // Giriş başarısız
                    return false;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Veritabanı bağlantısı kurulamadı!");
                return false;
            }
        }
        return false;
    }


    private void yatCikisYap() {

        String ruhsatnum = JOptionPane.showInputDialog(this, "Yat Ruhsat Numarası:");
        String yakit = JOptionPane.showInputDialog(this, "Yakıt (Litre):");

        if (ruhsatnum == null || yakit == null) {
            // Kullanıcı pencereyi kapattı veya iptal etti
            return;
        }

        if (ruhsatnum.isEmpty() || yakit.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen ruhsat numarasını ve yakıt bilgilerini doldurun!");
        } else {
            try {
                // Veritabanında ruhsatnum kontrolü
                baglanti = DriverManager.getConnection("jdbc:mysql://localhost:3306/yatlar", veritabanikullaniciadi, veritabanisifresi);
                PreparedStatement datakontrol = baglanti.prepareStatement("SELECT * FROM yatlar WHERE ruhsatnum = ?");
                datakontrol.setString(1, ruhsatnum);
                ResultSet sonucset = datakontrol.executeQuery();

                if (sonucset.next()) {
                    // Veritabanında kayıt var. Yat classını veritabanından aldığım bilgileri constructor içinde kullanarak dolduruyorum.
                    YatBilgisi yat = new YatBilgisi(
                            sonucset.getString("uzunluk"),
                            sonucset.getString("ad"),
                            sonucset.getString("telefon"),
                            sonucset.getDate("girisSaati")
                    );

                    Date cikisSaati = new Date();
                    SimpleDateFormat tarihformatii = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    String cikisSaatiStr = tarihformatii.format(cikisSaati);

                    double ekUcret = 0;

                    try {
                        double yakitMiktari = Double.parseDouble(yakit);
                        int uzunluk = Integer.parseInt(yat.getuzunluk());
                        double saatlikUcret;

                        if (uzunluk < 15) {
                            saatlikUcret = 7.0;
                        } else if (uzunluk > 15 && uzunluk <= 25) {
                            saatlikUcret = 12.5;
                        } else {
                            saatlikUcret = 16.0;
                        }

                        ekUcret = (cikisSaati.getTime() - yat.getGirisSaati().getTime()) / (1000 * 60 * 60.0) * saatlikUcret + yakitMiktari * 35.45;
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Geçerli bir yakıt miktarı girin!");
                        return;
                    }

                    String bicimlendirilmisucret = String.format("%.2f", ekUcret);

                    // Çıkış bilgilerini göster

                    JOptionPane.showMessageDialog(this, "Yat Çıkış Yapti \n" +
                            "Yat ruhsat numarası: " + ruhsatnum + "\n" +
                            "Yat Uzunluğu: " + yat.getuzunluk() + "\n" +
                            "Yat Sahibinin Adı: " + yat.getAd() + "\n" +
                            "Telefon Numarası: " + yat.getTelefon() + "\n" +
                            "Giriş Tarihi: " + yat.getGirisSaati() + "\n" +
                            "Çıkış Saati: " + cikisSaatiStr + "\n" +
                            "Yakıt Miktarı: " + yakit + " litre" + "\n" +
                            "Ücret: " + bicimlendirilmisucret + " TL");

                    // Veritabanından kaydı sil
                    PreparedStatement kaydisil = baglanti.prepareStatement("DELETE FROM yatlar WHERE ruhsatnum = ?");
                    kaydisil.setString(1, ruhsatnum);
                    kaydisil.executeUpdate();

                } else {
                    JOptionPane.showMessageDialog(this, "Veritabanında bu numaraya sahip bir kayıt bulunamadı.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Çıkış yapılırken bir hata oluştu! Hata Detayı: " + e.getMessage());
            } finally {
                try {
                    baglanti.close(); // Bağlantıyı kapat
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void yatlariListele() {

        try {
            // Veritabanına bağlan
            baglanti = DriverManager.getConnection("jdbc:mysql://localhost:3306/yatlar", veritabanikullaniciadi, veritabanisifresi);
            PreparedStatement listelemeyatlari = baglanti.prepareStatement("SELECT * FROM yatlar");
            ResultSet listelemeset = listelemeyatlari.executeQuery();

            // Veritabanındaki kayıtları içeren bir liste oluştur
            List<String[]> yatListesi = new ArrayList<>();
            while (listelemeset.next()) {
                String ruhsatnum = listelemeset.getString("ruhsatnum");
                String uzunluk = listelemeset.getString("uzunluk");
                String ad = listelemeset.getString("ad");
                String telefon = listelemeset.getString("telefon");
                String girisSaatiStr = listelemeset.getString("girisSaati");

                yatListesi.add(new String[]{ruhsatnum, uzunluk, ad, telefon, girisSaatiStr});
            }

            if (yatListesi.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Marinada kayıtlı yat bulunmamaktadır.");
                return;
            }

            // Liste verilerini kullanarak Array oluştur. toArray() metoduna parametre olarak boş bir String dizisi gönderiyoruz.
            // Bu sayede toArray() metodunun dönüş tipi String[][] oluyor. yazım kalıbı olarak toArray(new String[0][]) kullanılabilir.
            String[][] data = yatListesi.toArray(new String[0][]);
            String[] kolonadi = {"Yat Ruhsat Numarası", "Yat uzunluğu", "Yat Sahibinin Adı", "Telefon Numarası", "Giriş Saati"};

            JTable jtablo = new JTable(data, kolonadi);
            jtablo.getTableHeader().setBackground(Color.GRAY);

            JScrollPane kaydirma = new JScrollPane(jtablo);

            JOptionPane pane = new JOptionPane(kaydirma, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            JDialog diyalog = pane.createDialog(this, "Marinadaki Yatlar");
            diyalog.setSize(900, 300);
            diyalog.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Yatları listelerken bir hata oluştu! Hata Detayı: " + e.getMessage());
        } finally {
            try {
                if (baglanti != null) {
                    baglanti.close(); // Bağlantıyı kapat
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                new MarinaYatParkSistemi();
            }
        });
    }
}
//UYGULAMA AÇILINCA GELMESİNİ İSTEDİĞİMİZ DATALARIN YEDEĞİ
/*

INSERT INTO yatlar (ruhsatnum, uzunluk, ad, telefon, girisSaati)
VALUES
    ('34ZAP944', '24', 'Ahmet Akarca', '+905455673210', '2023-03-05 08:39:13'),
    ('ZR3788', '18', 'Adina Lorens', '+451234567891', '2023-02-01 09:45:37'),
    ('GH789', '14', 'Bill Guorgini', '+01674587628', '2023-04-07 10:15:00'),
    ('35JKL012', '36', 'Süleyman Demir', '+905444146953', '2023-04-08 11:30:19'),
    ('MNO345', '42', 'John Affrold', '+464718293', '2023-09-03 12:45:48'),
    ('PR678', '47', 'Ludgozky Malentin', '+901234567895', '2023-10-04 14:59:54'),
    ('ST901', '14', 'Ludmilla Kratiskov', '+7288790543', '2023-12-24 22:15:18'),
    ('01FFT234', '20', 'Aleyna Çelik', '+905436780912', '2023-12-09 16:30:23');

 */