import React, { useState } from 'react';
import { Container, Row, Col, Card, Button, Form, InputGroup, Modal } from 'react-bootstrap';
import { motion } from 'framer-motion';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import type { Variants } from 'framer-motion';
import { useNavigate } from "react-router-dom";

export default function Purchase() {
    const GMAIL_SMTP_HOST = "smtp.gmail.com";
    const GMAIL_SMTP_PORT = 587;

    // 1. State Tanımlamaları
    const [companyName, setCompanyName] = useState("");
    const [smtpUsername, setSmtpUsername] = useState("");
    const [email, setEmail] = useState("");
    const [taxNumber, setTaxNumber] = useState("");
    const [registrationNumber, setRegistrationNumber] = useState("");
    const [selectedPlan, setSelectedPlan] = useState<'baslangic' | 'buyuyen'>('buyuyen');
    const [smtpPassword, setSmtpPassword] = useState("");
    const [showSmtpModal, setShowSmtpModal] = useState(false);
    const [showResultModal, setShowResultModal] = useState(false);
    const [resultVariant, setResultVariant] = useState<"success" | "error">("success");
    const [resultTitle, setResultTitle] = useState("");
    const [resultMessage, setResultMessage] = useState("");
    const [resultHints, setResultHints] = useState<string[]>([]);
    const [validated, setValidated] = useState(false);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const planMapping = {
        baslangic: "STARTER",
        buyuyen: "PROFESSIONAL"
    };
    const navigate = useNavigate();

    // 2. Animasyon Varyasyonları
    const fadeInUp: Variants = {
        hidden: { opacity: 0, y: 30 },
        visible: { opacity: 1, y: 0, transition: { duration: 0.6, ease: "easeOut" } }
    };

    // 3. Dinamik Fiyat ve İçerik Yönetimi
    const plans = {
        baslangic: { name: 'Başlangıç Paketi', price: '990 ₺', period: '/ ay', tax: '198 ₺', total: '1.188 ₺' },
        buyuyen: { name: 'Büyüyen Paket (Popüler ✨)', price: '2.490 ₺', period: '/ ay', tax: '498 ₺', total: '2.988 ₺' }
    };

    const openResultModal = (
        variant: "success" | "error",
        title: string,
        message: string,
        hints: string[] = []
    ) => {
        setResultVariant(variant);
        setResultTitle(title);
        setResultMessage(message);
        setResultHints(hints);
        setShowResultModal(true);
    };

    const getPurchaseValidationHints = () => {
        const hints: string[] = [];
        const smtpUser = smtpUsername.trim().toLowerCase();
        const sender = email.trim().toLowerCase();

        if (!smtpUser.endsWith("@gmail.com") || !sender.endsWith("@gmail.com")) {
            hints.push("Lütfen Gmail uzantılı adres giriniz (örnek: adiniz@gmail.com).");
        }
        if (!/^\d{10}$/.test(taxNumber.trim())) {
            hints.push("Vergi numaranızı kontrol ediniz, 10 haneli giriniz (örnek: 0123456789).");
        }
        if (!registrationNumber.trim()) {
            hints.push("Ticaret sicil numarası boş bırakılamaz.");
        }
        if (!smtpPassword.trim()) {
            hints.push("SMTP uygulama şifresi boş olamaz.");
        }
        return hints;
    };

    // 4. Form Gönderme Fonksiyonu
    const submitPurchase = async () => {
        setIsSubmitting(true);
        try {
            const response = await fetch("http://localhost:9090/dev/v1/consumer/buyapplication", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    companyName: companyName,
                    smtpHost: GMAIL_SMTP_HOST,
                    smtpPort: GMAIL_SMTP_PORT,
                    smtpUsername: smtpUsername,
                    smtpPassword: smtpPassword,
                    fromEmail: email,
                    taxNumber: taxNumber,
                    registrationNumber: registrationNumber,
                    memberShipStatus: planMapping[selectedPlan]
                })
            });

            let baseResponse: { code?: number; message?: string } | null = null;
            try {
                baseResponse = await response.json();
            } catch {
                baseResponse = null;
            }

            if (response.ok && baseResponse?.code === 200) {
                setShowSmtpModal(false);
                openResultModal(
                    "success",
                    "Satın Alma İşlemi Tamamlandı",
                    baseResponse.message || "Satın alma işlemi başarıyla tamamlandı.",
                    ["Kurulum e-postası adresinize gönderilecektir."]
                );
            } else {
                const backendMessage = baseResponse?.message || "Satın alma işlemi başarısız oldu.";
                const hints = getPurchaseValidationHints();
                if (!hints.length) {
                    hints.push("Bilgilerinizi kontrol edip tekrar deneyiniz.");
                }
                openResultModal("error", "Satın Alma Başarısız", backendMessage, hints);
            }
        } catch (error) {
            openResultModal(
                "error",
                "Bağlantı Hatası",
                "Sunucuya bağlanırken bir sorun oluştu.",
                ["İnternet bağlantınızı kontrol edip tekrar deneyiniz.", `Teknik detay: ${(error as Error).message}`]
            );
        } finally {
            setIsSubmitting(false);
        }
    };

    const handlePaymentSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        const form = e.currentTarget;
        setValidated(true);
        if (!form.checkValidity()) {
            e.stopPropagation();
            return;
        }
        setShowSmtpModal(true);
    };

    const handleConfirmSmtpSettings = async () => {
        const hints = getPurchaseValidationHints();
        if (hints.length > 0) {
            openResultModal(
                "error",
                "Form Bilgileri Uygun Değil",
                "Satın alma işlemi başlatılamadı. Lütfen aşağıdaki alanları düzeltin.",
                hints
            );
            return;
        }
        await submitPurchase();
    };
    return (
        <div className="bg-light min-vh-100 py-5">
            <Container>
                {/* Geri Dön Butonu ve Logo */}
                <div className="d-flex justify-content-between align-items-center mb-5">
                    <Button variant="link" className="text-decoration-none text-secondary p-0" onClick={() => navigate(-1)}>
                        <i className="bi bi-arrow-left me-2"></i> Geri Dön
                    </Button>
                    <div className="d-flex align-items-center fw-bold text-primary fs-4" onClick={() => navigate('/')} style={{ cursor: 'pointer' }}>
                        <img alt="Peri" src="../../public/img/perilogo.png" style={{ height: "30px" }} />
                        <span className="ms-2">PERİ</span>
                    </div>
                </div>

                <Row className="g-4">
                    {/* SOL KOLON: ÖDEME VE FATURA FORMU */}
                    <Col lg={7} as={motion.div} initial="hidden" animate="visible" variants={fadeInUp}>
                        <Card className="border-0 shadow-sm rounded-4 p-4 mb-4">
                            <h4 className="fw-bold text-dark mb-4"><i className="bi bi-box-seam text-primary me-2"></i> 1. Paket Seçimi</h4>
                            <Row className="g-3">
                                <Col md={6}>
                                    <div
                                        className={`p-3 border rounded-4 style-pointer h-100 ${selectedPlan === 'baslangic' ? 'border-primary bg-primary bg-opacity-10' : 'bg-white'}`}
                                        onClick={() => setSelectedPlan('baslangic')}
                                        style={{ cursor: 'pointer', transition: '0.3s' }}
                                    >
                                        <Form.Check
                                            type="radio"
                                            id="plan-baslangic"
                                            label="Başlangıç Paketi"
                                            className="fw-bold mb-2"
                                            checked={selectedPlan === 'baslangic'}
                                            onChange={() => setSelectedPlan('baslangic')}
                                        />
                                        <p className="small text-secondary mb-0">0 - 15 Çalışan, Temel Modüller</p>
                                        <div className="fw-bold text-primary mt-2">990 ₺ <span className="small fw-normal text-muted">/ay</span></div>
                                    </div>
                                </Col>
                                <Col md={6}>
                                    <div
                                        className={`p-3 border rounded-4 style-pointer h-100 ${selectedPlan === 'buyuyen' ? 'border-primary bg-primary bg-opacity-10' : 'bg-white'}`}
                                        onClick={() => setSelectedPlan('buyuyen')}
                                        style={{ cursor: 'pointer', transition: '0.3s' }}
                                    >
                                        <Form.Check
                                            type="radio"
                                            id="plan-buyuyen"
                                            label="Büyüyen Paket ✨"
                                            className="fw-bold mb-2"
                                            checked={selectedPlan === 'buyuyen'}
                                            onChange={() => setSelectedPlan('buyuyen')}
                                        />
                                        <p className="small text-secondary mb-0">15 - 50 Çalışan, Tüm Modüller</p>
                                        <div className="fw-bold text-primary mt-2">2.490 ₺ <span className="small fw-normal text-muted">/ay</span></div>
                                    </div>
                                </Col>
                            </Row>
                            <p className="small text-muted mt-3 mb-0">
                                <i className="bi bi-info-circle me-1"></i> 50+ çalışan ve Kurumsal paket ihtiyaçlarınız için lütfen anasayfadan <strong>Teklif Al</strong> butonunu kullanın.
                            </p>
                        </Card>

                        <Card className="border-0 shadow-sm rounded-4 p-4">
                            <h4 className="fw-bold text-dark mb-4"><i className="bi bi-credit-card text-primary me-2"></i> 2. Kart ve Fatura Bilgileri ( ÖDEME SİMÜLASYONU)</h4>
                            <Form noValidate validated={validated} onSubmit={handlePaymentSubmit}>
                                <Row className="g-3">
                                    <Col md={12}>
                                        <Form.Group>
                                            <Form.Label className="small fw-medium text-secondary">Kart Üzerindeki İsim (ŞİRKET İSMİ LÜTFEN)</Form.Label>
                                            <Form.Control
                                                type="text"
                                                value={companyName}
                                                onChange={(e) => setCompanyName(e.target.value)}
                                                placeholder="X Şirketi Ltd. Şti."
                                                required
                                                className="rounded-3 py-2"
                                            />
                                        </Form.Group>
                                    </Col>
                                             <Col md={12}>
                                        <Form.Group>
                                            <Form.Label className="small fw-medium text-secondary">SMTP Kullanıcı Adı (Gmail adresiniz)</Form.Label>
                                            <InputGroup>
                                                <Form.Control
                                                    type="email"
                                                    value={smtpUsername}
                                                    onChange={(e) => setSmtpUsername(e.target.value)}
                                                    placeholder="@EMAIL"
                                                    required
                                                    className="rounded-3 py-2"
                                                />
                                                <InputGroup.Text className="bg-white rounded-end-3"><i className="bi bi-credit-card-2-front text-muted"></i></InputGroup.Text>
                                            </InputGroup>
                                        </Form.Group>
                                    </Col>
                                    <Col md={12}>
                                        <Form.Group>
                                            <Form.Label className="small fw-medium text-secondary">Gönderici E-Posta (Gmail adresiniz)</Form.Label>
                                            <InputGroup>
                                                <Form.Control
                                                    type="email"
                                                    value={email}
                                                    onChange={(e) => setEmail(e.target.value)}
                                                    placeholder="@EMAIL"
                                                    required
                                                    className="rounded-3 py-2"
                                                />
                                                <InputGroup.Text className="bg-white rounded-end-3"><i className="bi bi-credit-card-2-front text-muted"></i></InputGroup.Text>
                                            </InputGroup>
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group>
                                            <Form.Label className="small fw-medium text-secondary">Son Kullanma Tarihi(GEÇERLİ BİR VERGİ NUMARASI)</Form.Label>
                                            <Form.Control
                                                type="tel"
                                                value={taxNumber}
                                                onChange={(e) => setTaxNumber(e.target.value)}
                                                placeholder="1111111111"
                                                maxLength={10}
                                                required
                                                className="rounded-3 py-2"
                                            />
                                        </Form.Group>
                                    </Col>
                                    <Col md={6}>
                                        <Form.Group>
                                            <Form.Label className="small fw-medium text-secondary">CVC / CVC2(TİCARET SİCİL NUMARASI)</Form.Label>
                                            <Form.Control
                                                type="tel"
                                                maxLength={10}
                                                value={registrationNumber}
                                                onChange={(e) => setRegistrationNumber(e.target.value)}
                                                placeholder="***"
                                                required
                                                className="rounded-3 py-2"
                                            />
                                        </Form.Group>
                                    </Col>
                                </Row>

                                <Button variant="primary" type="submit" className="w-100 rounded-pill py-3 fw-bold shadow mt-4">
                                    Güvenli Ödeme Yap ({plans[selectedPlan].total})
                                </Button>
                                <p className="text-center text-muted small mt-3 mb-0">
                                    <i className="bi bi-shield-check text-success me-1"></i> 256-Bit SSL sertifikası ile ödemeniz tamamen güvendedir.
                                </p>
                            </Form>
                        </Card>
                    </Col>

                    {/* SAĞ KOLON: SİPARİŞ ÖZETİ */}
                    <Col lg={5} as={motion.div} initial="hidden" animate="visible" variants={fadeInUp}>
                        <Card className="border-0 shadow-sm rounded-4 p-4 sticky-top" style={{ top: '100px', zIndex: 10 }}>
                            <h4 className="fw-bold text-dark mb-4">Sipariş Özeti</h4>

                            <div className="d-flex justify-content-between align-items-center mb-3">
                                <div>
                                    <span className="fw-bold d-block text-dark">{plans[selectedPlan].name}</span>
                                    <span className="text-muted small">Aylık Abonelik</span>
                                </div>
                                <span className="fw-bold text-primary">{plans[selectedPlan].price}</span>
                            </div>

                            <hr className="text-muted opacity-25" />

                            <div className="d-flex justify-content-between text-secondary small mb-2">
                                <span>Ara Toplam</span>
                                <span>{plans[selectedPlan].price}</span>
                            </div>
                            <div className="d-flex justify-content-between text-secondary small mb-3">
                                <span>KDV (%20)</span>
                                <span>{plans[selectedPlan].tax}</span>
                            </div>

                            <div className="d-flex justify-content-between align-items-center bg-light p-3 rounded-3 mb-4">
                                <span className="fw-bold text-dark">Toplam Ödenecek</span>
                                <span className="fs-4 fw-bold text-primary">{plans[selectedPlan].total}</span>
                            </div>

                            <div className="bg-primary bg-opacity-10 p-3 rounded-4">
                                <h6 className="fw-bold text-primary mb-2"><i className="bi bi-check-circle-fill me-2"></i>Paket İçeriği:</h6>
                                <ul className="list-unstyled small text-secondary mb-0 d-flex flex-column gap-2">
                                    <li><i className="bi bi-check2 text-primary me-2"></i> Bulut Tabanlı İK Altyapısı</li>
                                    <li><i className="bi bi-check2 text-primary me-2"></i> Sınırsız Veri Depolama</li>
                                    <li><i className="bi bi-check2 text-primary me-2"></i> Mevzuat Uyumlu Dijital Arşiv</li>
                                    <li><i className="bi bi-check2 text-primary me-2"></i> Kolay Excel İçeri Aktarımı</li>
                                </ul>
                            </div>
                        </Card>
                    </Col>
                </Row>

                <Modal show={showSmtpModal} onHide={() => setShowSmtpModal(false)} centered>
                    <Modal.Header closeButton>
                        <Modal.Title>SMTP Ayarları</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <p className="small text-muted mb-3">
                            Bilgileriniz doğrulandı. Bu akışta sadece Gmail SMTP kullanılmaktadır. Lütfen normal Gmail şifresi değil, Google hesabınızdan ürettiğiniz App Password kullanın.
                        </p>
                        <Form.Group className="mb-3">
                            <Form.Label>SMTP Sağlayıcı</Form.Label>
                            <Form.Control type="text" value="Gmail" readOnly />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>SMTP Host</Form.Label>
                            <Form.Control
                                type="text"
                                value={GMAIL_SMTP_HOST}
                                readOnly
                            />
                        </Form.Group>
                        <Form.Group className="mb-3">
                            <Form.Label>SMTP Port</Form.Label>
                            <Form.Control
                                type="number"
                                value={GMAIL_SMTP_PORT}
                                readOnly
                            />
                        </Form.Group>
                        <Form.Group className="mb-0">
                            <Form.Label>Gönderici E-Posta</Form.Label>
                            <Form.Control type="email" value={email} readOnly />
                        </Form.Group>
                        <Form.Group className="mt-3 mb-0">
                            <Form.Label>SMTP Şifre / Uygulama Şifresi</Form.Label>
                            <Form.Control
                                type="password"
                                value={smtpPassword}
                                onChange={(e) => setSmtpPassword(e.target.value)}
                                placeholder="Google App Password girin"
                                required
                            />
                            <Form.Text className="text-muted">
                                Not: Google hesabınızda 2 adımlı doğrulama açık olmalı ve App Password oluşturulmuş olmalıdır.
                            </Form.Text>
                        </Form.Group>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowSmtpModal(false)}>
                            Vazgeç
                        </Button>
                        <Button variant="primary" onClick={handleConfirmSmtpSettings} disabled={isSubmitting}>
                            {isSubmitting ? "Gönderiliyor..." : "Onayla ve Satın Al"}
                        </Button>
                    </Modal.Footer>
                </Modal>

                <Modal
                    show={showResultModal}
                    onHide={() => {
                        setShowResultModal(false);
                        if (resultVariant === "success") {
                            navigate('/');
                        }
                    }}
                    centered
                >
                    <Modal.Header closeButton>
                        <Modal.Title className={resultVariant === "success" ? "text-success" : "text-danger"}>
                            {resultVariant === "success" ? "✅ " : "⚠️ "}
                            {resultTitle}
                        </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <p className="mb-2">{resultMessage}</p>
                        {resultHints.length > 0 && (
                            <ul className="mb-0 ps-3">
                                {resultHints.map((hint, index) => (
                                    <li key={index} className="small text-secondary mb-1">{hint}</li>
                                ))}
                            </ul>
                        )}
                    </Modal.Body>
                    <Modal.Footer>
                        <Button
                            variant={resultVariant === "success" ? "success" : "primary"}
                            onClick={() => {
                                setShowResultModal(false);
                                if (resultVariant === "success") {
                                    navigate('/');
                                }
                            }}
                        >
                            Tamam
                        </Button>
                    </Modal.Footer>
                </Modal>
            </Container>
        </div>
    );
}