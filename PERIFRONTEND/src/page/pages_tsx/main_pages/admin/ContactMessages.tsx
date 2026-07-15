import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../../../../components/header/Header";
import Footer from "../../../../components/footer/Footer";
import "../Application/App.css";
import "./ContactMessages.css";

type ContactMessage = {
    id: string;
    fullName: string;
    senderEmail: string;
    message: string;
    read: boolean;
    createdAt: string;
};

export default function ContactMessages() {
    const navigate = useNavigate();
    const [messages, setMessages] = useState<ContactMessage[]>([]);
    const [loading, setLoading] = useState(true);
    const [filter, setFilter] = useState<"all" | "unread">("all");

    const token = localStorage.getItem("token");

    useEffect(() => {
        if (!token) { navigate("/signin"); return; }
        fetch("http://localhost:9090/dev/v1/systemadministrator/contact-messages", {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(r => r.json())
            .then(data => setMessages(data.data || []))
            .catch(() => alert("Mesajlar yüklenemedi."))
            .finally(() => setLoading(false));
    }, []);

    const markRead = async (id: string) => {
        await fetch(`http://localhost:9090/dev/v1/systemadministrator/contact-messages/${id}/read`, {
            method: "PATCH",
            headers: { Authorization: `Bearer ${token}` }
        });
        setMessages(prev => prev.map(m => m.id === id ? { ...m, read: true } : m));
    };

    const displayed = filter === "unread" ? messages.filter(m => !m.read) : messages;
    const unreadCount = messages.filter(m => !m.read).length;

    return (
        <div className="app-wrapper">
            <Header />
            <div className="app-main">
                <div className="contact-messages-page">
                    <div className="cm-header">
                        <div>
                            <h2 className="cm-title">
                                <i className="bi bi-envelope me-2"></i>
                                İletişim Mesajları
                            </h2>
                            <p className="cm-subtitle">Landing sayfasından gönderilen mesajlar</p>
                        </div>
                        <div className="cm-filter-group">
                            <button
                                className={`cm-filter-btn ${filter === "all" ? "active" : ""}`}
                                onClick={() => setFilter("all")}
                            >
                                Tümü ({messages.length})
                            </button>
                            <button
                                className={`cm-filter-btn ${filter === "unread" ? "active" : ""}`}
                                onClick={() => setFilter("unread")}
                            >
                                Okunmamış
                                {unreadCount > 0 && <span className="cm-badge">{unreadCount}</span>}
                            </button>
                        </div>
                    </div>

                    {loading ? (
                        <div className="cm-loading">
                            <div className="spinner-border text-primary" role="status" />
                        </div>
                    ) : displayed.length === 0 ? (
                        <div className="cm-empty">
                            <i className="bi bi-inbox fs-1 text-secondary"></i>
                            <p className="text-secondary mt-2">
                                {filter === "unread" ? "Okunmamış mesaj yok." : "Henüz mesaj bulunmuyor."}
                            </p>
                        </div>
                    ) : (
                        <div className="cm-list">
                            {displayed.map(msg => (
                                <div key={msg.id} className={`cm-card ${!msg.read ? "cm-card--unread" : ""}`}>
                                    <div className="cm-card-header">
                                        <div className="cm-sender">
                                            <div className="cm-avatar">
                                                {msg.fullName.charAt(0).toUpperCase()}
                                            </div>
                                            <div>
                                                <div className="cm-name">{msg.fullName}</div>
                                                <a href={`mailto:${msg.senderEmail}`} className="cm-email">
                                                    {msg.senderEmail}
                                                </a>
                                            </div>
                                        </div>
                                        <div className="cm-meta">
                                            <span className="cm-date">
                                                {new Date(msg.createdAt).toLocaleString("tr-TR")}
                                            </span>
                                            {!msg.read && (
                                                <button className="cm-read-btn" onClick={() => markRead(msg.id)}>
                                                    <i className="bi bi-check2-all me-1"></i>Okundu
                                                </button>
                                            )}
                                            {msg.read && (
                                                <span className="cm-read-badge">
                                                    <i className="bi bi-check2-all me-1"></i>Okundu
                                                </span>
                                            )}
                                        </div>
                                    </div>
                                    <div className="cm-message">{msg.message}</div>
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
