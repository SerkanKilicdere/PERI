import React from 'react';
import { Navbar, Nav, Container, Row, Col, Button, Card, Table, Modal, Form } from 'react-bootstrap';
import { motion } from 'framer-motion';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import './Landing.css';
import { useNavigate } from "react-router-dom";
import type {Variants} from 'framer-motion';

const Landing: React.FC = () => {
    const navigate = useNavigate();
    const [contactName, setContactName] = React.useState("");
    const [contactEmail, setContactEmail] = React.useState("");
    const [contactMessage, setContactMessage] = React.useState("");
    const [contactStatus, setContactStatus] = React.useState<"idle" | "sending" | "success" | "error">("idle");

    const handleContactSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setContactStatus("sending");
        try {
            const response = await fetch("http://localhost:9090/dev/v1/consumer/contact", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ fullName: contactName, senderEmail: contactEmail, message: contactMessage })
            });
            if (!response.ok) {
                const err = await response.json().catch(() => ({}));
                setContactStatus("error");
                alert(err.message || "Mesaj gönderilemedi, lütfen tekrar deneyin.");
                return;
            }
            setContactStatus("success");
            setContactName("");
            setContactEmail("");
            setContactMessage("");
        } catch {
            setContactStatus("error");
            alert("Bağlantı hatası, lütfen tekrar deneyin.");
        }
    };

    const [showQuoteModal, setShowQuoteModal] = React.useState(false);
    const [quoteForm, setQuoteForm] = React.useState({ fullName: "", email: "", phone: "", companyName: "", employeeCount: "", notes: "" });
    const [quoteStatus, setQuoteStatus] = React.useState<"idle" | "sending" | "success">("idle");

    const handleQuoteChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        setQuoteForm(prev => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const handleQuoteSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setQuoteStatus("sending");
        try {
            const res = await fetch("http://localhost:9090/dev/v1/consumer/quote", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ ...quoteForm, employeeCount: Number(quoteForm.employeeCount) })
            });
            if (!res.ok) { setQuoteStatus("idle"); alert("Bir hata oluştu, lütfen tekrar deneyin."); return; }
            setQuoteStatus("success");
        } catch {
            setQuoteStatus("idle");
            alert("Bağlantı hatası, lütfen tekrar deneyin.");
        }
    };

    const fadeInUp: Variants = {
        hidden: { opacity: 0, y: 40 },
        visible: { opacity: 1, y: 0, transition: { duration: 0.8, ease: "easeOut" } }
    };

    // Aşağı kaydırdıkça animasyonların tetiklenmesi için konfigürasyon


    const featureList = [
        {
            title: "Dijital Özlük Dosyası",
            text: "Tüm personel evraklarını kağıtsız bir ortamda, güvenle yönetin.",
            img: "https://images.unsplash.com/photo-1554224154-26032ffc0d07?auto=format&fit=crop&w=800&q=80",
            icon: "bi-folder-check"
        },
        {
            title: "İzin ve Mesai Takibi",
            text: "Karmaşık excel tablolarına son verin, talepleri tek tıkla onaylayın.",
            img: "https://images.unsplash.com/photo-1506784983877-45594efa4cbe?auto=format&fit=crop&w=800&q=80",
            icon: "bi-calendar-range"
        },
        {
            title: "Performans Analizi",
            text: "Ekibinizin verimliliğini somut datalar ve grafiklerle ölçümleyin.",
            img: "https://images.unsplash.com/photo-1551288049-bebda4e38f71?auto=format&fit=crop&w=800&q=80",
            icon: "bi-graph-up-arrow"
        },
        {
            title: "Modern İşe Alım",
            text: "Aday havuzunuzu genişletin ve en doğru yeteneği hızla bulun.",
            img: "https://images.unsplash.com/photo-1573497161161-c3e73707e25c?auto=format&fit=crop&w=800&q=80",
            icon: "bi-person-plus"
        }
    ];

    const steps = [
        { title: "1. Kolay Kayıt", desc: "Dakikalar içinde firmanızı sisteme güvenle tanımlayın." },
        { title: "2. Veri Aktarımı", desc: "Mevcut personel listenizi Excel listelerinizle tek seferde içeri aktarın." },
        { title: "3. Yetkilendirme", desc: "Yöneticilerinizi ve çalışan rollerini pratik şekilde belirleyin." },
        { title: "4. Kullanıma Hazır!", desc: "Tüm İK süreçlerinizi bulut tabanlı sistemimizden keyifle yönetin." }
    ];

    return (
        <div className="bg-light min-vh-100">
            {/* NAVBAR */}
            <Navbar bg="white" expand="lg" className="sticky-top shadow-sm py-3">
                <Container>
                    <Navbar.Brand href="#" className="fw-bold text-primary fs-4 d-flex align-items-center gap-2">
                        <div className="logo d-flex align-items-center">
                            <img alt="Peri" src="../../public/img/perilogo.png" style={{ height: "30px" }} />
                            <span className="ms-2">PERİ</span>
                        </div>
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav">
                        <Nav className="ms-auto fw-medium">
                            {/* href değerleri "#id" şeklinde verildikten sonra CSS'teki smooth scroll sayesinde kayarak odaklanır */}
                            <Nav.Link href="#features">Özellikler</Nav.Link>
                            <Nav.Link href="#how-it-works">Nasıl Çalışır</Nav.Link>
                            <Nav.Link href="#pricing">Fiyatlandırma</Nav.Link>
                            <Nav.Link href="#contact">İletişim</Nav.Link>

                            <Button variant="outline-primary" className="ms-lg-3 me-2 rounded-pill px-4" onClick={() => navigate('/satinAl')}>Satın Al</Button>
                            <Button variant="outline-primary" className="ms-lg-3 me-2 rounded-pill px-4" onClick={() => navigate('/SignIn')}>Giriş</Button>
                            <Button variant="primary" className="rounded-pill px-4 shadow-sm">Ücretsiz Dene</Button>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>

            {/* HERO SECTION */}
            <header className="py-5 bg-white border-bottom">
                <Container className="py-lg-5">
                    <Row className="align-items-center">
                        <Col lg={6} as={motion.div} initial="hidden" animate="visible" variants={fadeInUp}>
                            <h1 className="display-4 fw-bold text-dark mb-4">
                                İnsan Kaynaklarını <span className="text-primary">Geleceğe Taşıyın</span>
                            </h1>
                            <p className="lead text-secondary mb-4">
                                Modern şirketler için tasarlanmış hepsi bir arada İK yazılımı. Ekibinizi yönetmek hiç bu kadar keyifli olmamıştı.
                            </p>
                            <div className="d-flex gap-3">
                                <Button href="#features" variant="primary" size="lg" className="rounded-pill px-4 py-3 shadow">
                                    Şimdi Keşfet
                                </Button>
                                <Button variant="light" size="lg" className="rounded-pill px-4 py-3 border">
                                    Demoyu İzle
                                </Button>
                            </div>
                        </Col>
                        <Col lg={6} className="mt-5 mt-lg-0 text-center" as={motion.div} initial={{opacity:0, scale:0.9}} animate={{opacity:1, scale:1}} transition={{duration: 0.8}}>
                            <img
                                src="https://images.unsplash.com/photo-1522071820081-009f0129c71c?auto=format&fit=crop&w=1000&q=80"
                                alt="HR Team"
                                className="img-fluid rounded-4 shadow-lg"
                                style={{ maxHeight: "400px", objectFit: "cover", width: "100%" }}
                            />
                        </Col>
                    </Row>
                </Container>
            </header>

            {/* ÖZELLİKLER (FEATURES) SEKMESİ */}
            <section id="features" className="py-5 bg-light">
                <Container className="py-5">
                    <div className="text-center mb-5">
                        <h6 className="text-primary fw-bold text-uppercase">Çözümlerimiz</h6>
                        <h2 className="display-6 fw-bold">Ekibiniz İçin Her Şey Hazır</h2>
                    </div>

                    <Row className="g-4">
                        {featureList.map((item, index) => (
                            <Col md={6} lg={3} key={index}>
                                <motion.div
                                    variants={fadeInUp}
                                    initial="hidden"
                                    whileInView="visible"
                                    viewport={{ once: true, margin: "-100px" }}
                                >
                                    <Card className="border-0 shadow-sm h-100 rounded-4 overflow-hidden">
                                        <Card.Img variant="top" src={item.img} style={{ height: "180px", objectFit: "cover" }} />
                                        <Card.Body className="p-4">
                                            <div className="text-primary mb-2 fs-4">
                                                <i className={`bi ${item.icon}`}></i>
                                            </div>
                                            <Card.Title className="fw-bold mb-2">{item.title}</Card.Title>
                                            <Card.Text className="text-muted small">
                                                {item.text}
                                            </Card.Text>
                                        </Card.Body>
                                    </Card>
                                </motion.div>
                            </Col>
                        ))}
                    </Row>
                </Container>
            </section>

            {/* NASIL ÇALIŞIR (HOW IT WORKS) SEKMESİ */}
            <section id="how-it-works" className="py-5 bg-white">
                <Container className="py-5">
                    <div className="text-center mb-5">
                        <h6 className="text-primary fw-bold text-uppercase">Süreç Nasıl İlerliyor?</h6>
                        <h2 className="display-6 fw-bold">4 Adımda PERİ İK ile Tanışın</h2>
                    </div>

                    <Row className="g-4">
                        {steps.map((step, index) => (
                            <Col md={6} lg={3} key={index}>
                                <motion.div
                                    variants={fadeInUp}
                                    initial="hidden"
                                    whileInView="visible"
                                    viewport={{ once: true, margin: "-100px" }}
                                    className="p-4 border rounded-4 bg-light h-100"
                                >
                                    <h4 className="fw-bold text-primary mb-3">{step.title}</h4>
                                    <p className="text-secondary small mb-0">{step.desc}</p>
                                </motion.div>
                            </Col>
                        ))}
                    </Row>
                </Container>
            </section>

            {/* FİYATLANDIRMA (PRICING) SEKMESİ */}
            <section id="pricing" className="py-5 bg-light">
                <Container className="py-5">
                    <div className="text-center mb-5">
                        <h6 className="text-primary fw-bold text-uppercase">Fiyat Listesi</h6>
                        <h2 className="display-6 fw-bold">Şirket Büyüklüğünüze Göre Şeffaf Fiyatlar</h2>
                    </div>

                    <motion.div
                        variants={fadeInUp}
                        initial="hidden"
                        whileInView="visible"
                        viewport={{ once: true }}
                        className="table-responsive shadow-sm bg-white rounded-4 p-4 border"
                    >
                        <Table hover borderless className="align-middle mb-0">
                            <thead className="table-light">
                            <tr className="text-secondary">
                                <th>Paket</th>
                                <th>Çalışan Sınırı</th>
                                <th>Modüller</th>
                                <th>Destek Kanalı</th>
                                <th className="text-end">Aylık Ücret</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td><strong className="text-dark">Başlangıç</strong></td>
                                <td>0 - 15 Kişi</td>
                                <td>Özlük Dosyası, Temel İzin Yönetimi</td>
                                <td>E-Posta Desteği</td>
                                <td className="text-end fw-bold text-primary">990 ₺</td>
                            </tr>
                            <tr className="bg-primary bg-opacity-10">
                                <td><strong className="text-dark">Büyüyen (Popüler ✨)</strong></td>
                                <td>15 - 50 Kişi</td>
                                <td>Tüm Modüller + Performans Analizi</td>
                                <td>7/24 Telefon & Ticket</td>
                                <td className="text-end fw-bold text-primary">2.490 ₺</td>
                            </tr>
                            <tr>
                                <td><strong className="text-dark">Kurumsal</strong></td>
                                <td>Sınırsız</td>
                                <td>Özel Entegrasyonlar & Dedicated Sunucu</td>
                                <td>Müşteri Temsilcisi</td>
                                <td className="text-end fw-bold text-primary">
                                    <Button variant="primary" className="rounded-pill px-3 shadow-sm" onClick={() => { setShowQuoteModal(true); setQuoteStatus("idle"); }}>
                                        TEKLİF AL
                                    </Button>
                                </td>
                            </tr>
                            </tbody>
                        </Table>
                    </motion.div>

                </Container>

            </section>

            {/* İLETİŞİM (CONTACT) SEKMESİ */}
            <section id="contact" className="py-5 bg-white">
                <Container className="py-5">
                    <Row className="align-items-center g-5">
                        <Col lg={6} as={motion.div} initial="hidden" whileInView="visible" viewport={{ once: true }} variants={fadeInUp}>
                            <h6 className="text-primary fw-bold text-uppercase">Bize Ulaşın</h6>
                            <h2 className="display-6 fw-bold mb-4">Kafanızda Soru İşaretleri Mi Var?</h2>
                            <p className="text-secondary mb-4">
                                Destek ekibimiz merak ettiğiniz her türlü teknik ve finansal soruyu memnuniyetle cevaplamaya hazır.
                            </p>
                            <div className="d-flex flex-column gap-3 fs-5">
                                <div className="d-flex align-items-center gap-3">
                                    <i className="bi bi-geo-alt-fill text-primary"></i>
                                    <span className="text-secondary fs-6">Maslak Teknokent, No: 34 İstanbul</span>
                                </div>
                                <div className="d-flex align-items-center gap-3">
                                    <i className="bi bi-telephone-fill text-primary"></i>
                                    <span className="text-secondary fs-6">+90 (212) 555 01 90</span>
                                </div>
                                <div className="d-flex align-items-center gap-3">
                                    <i className="bi bi-envelope-fill text-primary"></i>
                                    <span className="text-secondary fs-6">info@peripms.com</span>
                                </div>
                            </div>
                        </Col>
                        <Col lg={6} as={motion.div} initial="hidden" whileInView="visible" viewport={{ once: true }} variants={fadeInUp} className="text-center">
                            <div className="p-4 border rounded-4 bg-light shadow-sm">
                                <h4 className="fw-bold mb-3 text-dark">Hızlı Mesaj Gönderin</h4>
                                {contactStatus === "success" ? (
                                    <div className="alert alert-success rounded-3 text-center">
                                        <i className="bi bi-check-circle me-2"></i>
                                        Mesajınız başarıyla iletildi! En kısa sürede geri dönüş yapacağız.
                                    </div>
                                ) : (
                                <form className="d-flex flex-column gap-3 text-start" onSubmit={handleContactSubmit}>
                                    <div>
                                        <label className="form-label small text-secondary">Ad Soyad</label>
                                        <input type="text" className="form-control rounded-3" placeholder="Örn: Ahmet Yılmaz"
                                               value={contactName} onChange={e => setContactName(e.target.value)} required />
                                    </div>
                                    <div>
                                        <label className="form-label small text-secondary">E-Posta</label>
                                        <input type="email" className="form-control rounded-3" placeholder="ahmet@sirket.com"
                                               value={contactEmail} onChange={e => setContactEmail(e.target.value)} required />
                                    </div>
                                    <div>
                                        <label className="form-label small text-secondary">Mesajınız</label>
                                        <textarea className="form-control rounded-3" rows={3} placeholder="Sorularınızı buraya yazın..."
                                                  value={contactMessage} onChange={e => setContactMessage(e.target.value)} required></textarea>
                                    </div>
                                    <Button variant="primary" type="submit" className="rounded-pill mt-2" disabled={contactStatus === "sending"}>
                                        {contactStatus === "sending" ? "Gönderiliyor..." : "Gönder"}
                                    </Button>
                                </form>
                                )}
                            </div>
                        </Col>
                    </Row>
                </Container>
            </section>

            {/* FOOTER */}
            <footer className="bg-dark text-white py-5">
                <Container className="text-center">
                    <p className="mb-0 opacity-75">© 2026 PERI PMS Yazılım. Tüm hakları saklıdır.</p>
                </Container>
            </footer>

            {/* TEKLİF AL MODAL */}
            <Modal show={showQuoteModal} onHide={() => setShowQuoteModal(false)} centered size="lg">
                <Modal.Header closeButton className="border-0 pb-0">
                    <Modal.Title className="fw-bold fs-5">
                        <i className="bi bi-file-earmark-text me-2 text-primary"></i>
                        Kurumsal Teklif Talebi
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body className="px-4 pb-4">
                    {quoteStatus === "success" ? (
                        <div className="text-center py-4">
                            <div style={{ fontSize: "3rem" }}>🎉</div>
                            <h5 className="fw-bold mt-3">Talebiniz Alındı!</h5>
                            <p className="text-secondary">Ekibimiz en kısa sürede sizinle iletişime geçecek.</p>
                            <Button variant="primary" className="rounded-pill px-4 mt-2" onClick={() => setShowQuoteModal(false)}>
                                Tamam
                            </Button>
                        </div>
                    ) : (
                        <Form onSubmit={handleQuoteSubmit}>
                            <Row className="g-3">
                                <Col md={6}>
                                    <Form.Label className="small text-secondary fw-medium">Ad Soyad <span className="text-danger">*</span></Form.Label>
                                    <Form.Control className="rounded-3" placeholder="Ahmet Yılmaz" name="fullName" value={quoteForm.fullName} onChange={handleQuoteChange} required />
                                </Col>
                                <Col md={6}>
                                    <Form.Label className="small text-secondary fw-medium">E-Posta <span className="text-danger">*</span></Form.Label>
                                    <Form.Control type="email" className="rounded-3" placeholder="ahmet@sirket.com" name="email" value={quoteForm.email} onChange={handleQuoteChange} required />
                                </Col>
                                <Col md={6}>
                                    <Form.Label className="small text-secondary fw-medium">Telefon <span className="text-danger">*</span></Form.Label>
                                    <Form.Control className="rounded-3" placeholder="+90 555 000 00 00" name="phone" value={quoteForm.phone} onChange={handleQuoteChange} required />
                                </Col>
                                <Col md={6}>
                                    <Form.Label className="small text-secondary fw-medium">Şirket Adı <span className="text-danger">*</span></Form.Label>
                                    <Form.Control className="rounded-3" placeholder="ABC Teknoloji A.Ş." name="companyName" value={quoteForm.companyName} onChange={handleQuoteChange} required />
                                </Col>
                                <Col md={6}>
                                    <Form.Label className="small text-secondary fw-medium">Çalışan Sayısı <span className="text-danger">*</span></Form.Label>
                                    <Form.Control type="number" min={1} className="rounded-3" placeholder="Örn: 250" name="employeeCount" value={quoteForm.employeeCount} onChange={handleQuoteChange} required />
                                </Col>
                                <Col md={12}>
                                    <Form.Label className="small text-secondary fw-medium">Ek Notlar</Form.Label>
                                    <Form.Control as="textarea" rows={3} className="rounded-3" placeholder="Özel gereksinimleriniz, entegrasyon ihtiyaçlarınız..." name="notes" value={quoteForm.notes} onChange={handleQuoteChange} />
                                </Col>
                                <Col md={12} className="d-flex justify-content-end gap-2 mt-2">
                                    <Button variant="outline-secondary" className="rounded-pill px-4" onClick={() => setShowQuoteModal(false)}>
                                        Vazgeç
                                    </Button>
                                    <Button variant="primary" type="submit" className="rounded-pill px-4 shadow-sm" disabled={quoteStatus === "sending"}>
                                        {quoteStatus === "sending" ? "Gönderiliyor..." : "Teklif Talep Et"}
                                    </Button>
                                </Col>
                            </Row>
                        </Form>
                    )}
                </Modal.Body>
            </Modal>
        </div>
    );
};

export default Landing;