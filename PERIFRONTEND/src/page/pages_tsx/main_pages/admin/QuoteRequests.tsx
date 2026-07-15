import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../../../../components/header/Header";
import Footer from "../../../../components/footer/Footer";
import "../Application/App.css";
import "./QuoteRequests.css";

type QuoteRequest = {
    id: string;
    fullName: string;
    email: string;
    phone: string;
    companyName: string;
    employeeCount: number;
    notes: string | null;
    read: boolean;
    createdAt: string;
};

export default function QuoteRequests() {
    const navigate = useNavigate();
    const [requests, setRequests] = useState<QuoteRequest[]>([]);
    const [loading, setLoading] = useState(true);
    const [filter, setFilter] = useState<"all" | "unread">("all");
    const [expanded, setExpanded] = useState<string | null>(null);

    const token = localStorage.getItem("token");

    useEffect(() => {
        if (!token) { navigate("/signin"); return; }
        fetch("http://localhost:9090/dev/v1/systemadministrator/quote-requests", {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(r => r.json())
            .then(data => setRequests(data.data || []))
            .catch(() => alert("Teklif talepleri yüklenemedi."))
            .finally(() => setLoading(false));
    }, []);

    const markRead = async (id: string) => {
        await fetch(`http://localhost:9090/dev/v1/systemadministrator/quote-requests/${id}/read`, {
            method: "PATCH",
            headers: { Authorization: `Bearer ${token}` }
        });
        setRequests(prev => prev.map(r => r.id === id ? { ...r, read: true } : r));
    };

    const displayed = filter === "unread" ? requests.filter(r => !r.read) : requests;
    const unreadCount = requests.filter(r => !r.read).length;

    return (
        <div className="app-wrapper">
            <Header />
            <div className="app-main">
                <div className="qr-page">
                    <div className="qr-header">
                        <div>
                            <h2 className="qr-title">
                                <i className="bi bi-file-earmark-text me-2"></i>
                                Kurumsal Teklif Talepleri
                            </h2>
                            <p className="qr-subtitle">Potansiyel müşterilerin teklif talepleri</p>
                        </div>
                        <div className="qr-filter-group">
                            <button className={`qr-filter-btn ${filter === "all" ? "active" : ""}`} onClick={() => setFilter("all")}>
                                Tümü ({requests.length})
                            </button>
                            <button className={`qr-filter-btn ${filter === "unread" ? "active" : ""}`} onClick={() => setFilter("unread")}>
                                Yeni {unreadCount > 0 && <span className="qr-badge">{unreadCount}</span>}
                            </button>
                        </div>
                    </div>

                    {loading ? (
                        <div className="qr-loading"><div className="spinner-border text-primary" /></div>
                    ) : displayed.length === 0 ? (
                        <div className="qr-empty">
                            <i className="bi bi-inbox fs-1 text-secondary"></i>
                            <p className="text-secondary mt-2">{filter === "unread" ? "Yeni teklif talebi yok." : "Henüz teklif talebi bulunmuyor."}</p>
                        </div>
                    ) : (
                        <div className="qr-list">
                            {displayed.map(req => (
                                <div key={req.id} className={`qr-card ${!req.read ? "qr-card--unread" : ""}`}>
                                    <div className="qr-card-header" onClick={() => setExpanded(expanded === req.id ? null : req.id)} style={{ cursor: "pointer" }}>
                                        <div className="qr-sender">
                                            <div className="qr-avatar">{req.fullName.charAt(0).toUpperCase()}</div>
                                            <div>
                                                <div className="qr-name">{req.fullName}</div>
                                                <div className="qr-company">
                                                    <i className="bi bi-building me-1"></i>{req.companyName}
                                                    <span className="qr-emp-count ms-2">
                                                        <i className="bi bi-people me-1"></i>{req.employeeCount} çalışan
                                                    </span>
                                                </div>
                                            </div>
                                        </div>
                                        <div className="qr-meta">
                                            <span className="qr-date">{new Date(req.createdAt).toLocaleString("tr-TR")}</span>
                                            {!req.read && (
                                                <button className="qr-read-btn" onClick={e => { e.stopPropagation(); markRead(req.id); }}>
                                                    <i className="bi bi-check2-all me-1"></i>Okundu
                                                </button>
                                            )}
                                            {req.read && <span className="qr-read-badge"><i className="bi bi-check2-all me-1"></i>Okundu</span>}
                                            <i className={`bi bi-chevron-${expanded === req.id ? "up" : "down"} text-secondary`}></i>
                                        </div>
                                    </div>

                                    {expanded === req.id && (
                                        <div className="qr-details">
                                            <div className="qr-detail-grid">
                                                <div className="qr-detail-item">
                                                    <span className="qr-detail-label"><i className="bi bi-envelope me-1"></i>E-Posta</span>
                                                    <a href={`mailto:${req.email}`} className="qr-detail-value link">{req.email}</a>
                                                </div>
                                                <div className="qr-detail-item">
                                                    <span className="qr-detail-label"><i className="bi bi-telephone me-1"></i>Telefon</span>
                                                    <a href={`tel:${req.phone}`} className="qr-detail-value link">{req.phone}</a>
                                                </div>
                                                <div className="qr-detail-item">
                                                    <span className="qr-detail-label"><i className="bi bi-building me-1"></i>Şirket</span>
                                                    <span className="qr-detail-value">{req.companyName}</span>
                                                </div>
                                                <div className="qr-detail-item">
                                                    <span className="qr-detail-label"><i className="bi bi-people me-1"></i>Çalışan</span>
                                                    <span className="qr-detail-value">{req.employeeCount} kişi</span>
                                                </div>
                                            </div>
                                            {req.notes && (
                                                <div className="qr-notes">
                                                    <span className="qr-detail-label mb-1 d-block"><i className="bi bi-chat-left-text me-1"></i>Ek Notlar</span>
                                                    <p className="qr-notes-text">{req.notes}</p>
                                                </div>
                                            )}
                                        </div>
                                    )}
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
            <Footer />
        </div>
    );
}
