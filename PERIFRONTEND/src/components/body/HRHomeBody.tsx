import "./HRHomeBody.css";

export default function HRHomeBody() {
    return (
        <div className="employee-body-container">
            <h1>Modern HR Yönetimi</h1>
            <p>
                Peri HR ile çalışan yönetimi, izin süreçleri, performans değerlendirme ve
                daha fazlasını tek bir panelden yönetin.
            </p>

            <div className="feature-cards">
                <div className="card-item">
                    <h3>Çalışan Yönetimi</h3>
                    <p>Personel bilgilerini kolayca yönetin.</p>
                </div>

                <div className="card-item">
                    <h3>İzin Takibi</h3>
                    <p>İzin taleplerini hızlıca onaylayın.</p>
                </div>

                <div className="card-item">
                    <h3>Performans</h3>
                    <p>Performans değerlendirmelerini yönetin.</p>
                </div>
            </div>
        </div>
    );
}
