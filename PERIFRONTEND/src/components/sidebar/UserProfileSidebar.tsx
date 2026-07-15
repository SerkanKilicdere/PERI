import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAddressCard, faUser } from "@fortawesome/free-solid-svg-icons";
import "./UserProfileSidebar.css";

interface UserProfileSidebarProps {
    activeTab: "basic" | "personal";
    setActiveTab: (tab: "basic" | "personal") => void;
}

export default function UserProfileSidebar({ activeTab, setActiveTab }: UserProfileSidebarProps) {
    return (
        <aside className="user-profile-sidebar-container">
            <button
                className={`user-profile-sidebar-button ${activeTab === "basic" ? "active" : ""}`}
                onClick={() => setActiveTab("basic")}
                type="button"
            >
                <FontAwesomeIcon icon={faUser} />
                <span>Temel Bilgi</span>
            </button>
            <button
                className={`user-profile-sidebar-button ${activeTab === "personal" ? "active" : ""}`}
                onClick={() => setActiveTab("personal")}
                type="button"
            >
                <FontAwesomeIcon icon={faAddressCard} />
                <span>Kişisel & Banka</span>
            </button>
        </aside>
    );
}

