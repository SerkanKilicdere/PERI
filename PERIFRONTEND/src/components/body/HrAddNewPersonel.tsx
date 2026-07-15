import "./HrAddNewPersonel.css";
import * as React from "react";
import {useState} from "react";

export default function HrAddNewPersonel() {

    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [phone, setPhone] = useState("");
    const [position, setPosition] = useState("");


    const addNewPersonel = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();


        try {
            const response = await fetch("http://localhost:9090/dev/v1/humanresource/recorduser", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    firstName: firstName,
                    lastName: lastName,
                    emailAddress: email,
                    phoneNumber: phone,
                    userCategories: position
                })
            });

            if (response.ok) {
                alert("Kayıt Başarılı!");
            } else {
                alert("Kayıt başarısız!");
            }
        } catch (error) {
            console.error("Hata oluştu:", error);
            alert("Sunucuya bağlanılamadı.");
        }
    };

    return (
        <div className="add-employee-body-container">
            <div className="employee-form-card">
                <h2 className="employee-form-title">Yeni Personel Ekle</h2>
                <form onSubmit={addNewPersonel}>
                    <div className="mb-3">
                        <label className="form-label">Adınız</label>
                        <input
                            className="form-control"
                            type="text"
                            value={firstName}
                            onChange={(e) => setFirstName(e.target.value)}
                            placeholder="Adınız"
                        />
                    </div>

                    <div className="mb-3">
                        <label className="form-label">Soyadınız</label>
                        <input
                            className="form-control"
                            type="text"
                            value={lastName}
                            onChange={(e) => setLastName(e.target.value)}
                            placeholder="Soyadınız"
                        />
                    </div>

                    <div className="mb-3">
                        <label className="form-label">E-posta Adresiniz</label>
                        <input
                            className="form-control"
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            placeholder="E-posta"
                        />
                    </div>

                    <div className="mb-3">
                        <label className="form-label">Telefon Numaranız</label>
                        <input
                            className="form-control"
                            type="tel"
                            value={phone}
                            onChange={(e) => setPhone(e.target.value)}
                            placeholder="Telefon"
                        />
                    </div>

                    <div className="mb-4">
                        <label className="form-label">Pozisyon</label>
                        <select
                            className="form-select"
                            value={position}
                            onChange={(e) => setPosition(e.target.value)}
                        >
                            <option value="">Seçiniz...</option>
                            <option value="EXECUTIVE">Genel Müdür</option>
                            <option value="BOARD_MEMBER">Direktör</option>
                            <option value="STRATEGIC_PLANNING">Strateji Geliştirme</option>
                            <option value="REGIONAL_MANAGER">Bölge Müdürü</option>
                            <option value="DEPARTMENT_HEAD">Departman Müdürü</option>
                            <option value="TECH_IT">Bilişim Personeli</option>
                            <option value="DATA_ANALYTICS">Data Analist</option>
                            <option value="CYBER_SECURITY">Siber Güvenlik</option>
                            <option value="SUPPLY_CHAIN">Teadrik Zinciri</option>
                            <option value="MANUFACTURING">Üretim</option>
                            <option value="QUALITY_CONTROL">Kalite Kontrol</option>
                            <option value="SALES_RETAIL">Satış</option>
                            <option value="MARKETING_BRAND">Pazarlama</option>
                            <option value="PUBLIC_RELATIONS">Halkla İlişkiler</option>
                            <option value="HUMAN_RESOURCES">İnsan Kaynakları</option>
                            <option value="FINANCE_TREASURY">Finans</option>
                            <option value="LEGAL_COMPLIANCE">Hukuk</option>
                            <option value="SUSTAINABILITY">İş Güvenliği</option>
                            <option value="RESEARCH_DEV">Ar-Ge</option>
                            <option value="GENERAL_LABOR">İşçi</option>
                            <option value="WAREHOUSE_STAFF">Depo</option>
                            <option value="DISTRIBUTION_ENTRY">Lojistik</option>
                            <option value="FACILITY_MAINTENANCE">Temizlik</option>
                            <option value="SEASONAL_WORKER">Sözleşmeli</option>
                            <option value="CONTRACTOR">Danışman</option>
                            <option value="FIELD_OPERATIVE">Saha</option>

                        </select>
                    </div>

                    <button type="submit" className="btn btn-primary">Kaydet</button>
                </form>
            </div>
        </div>
    );
}
