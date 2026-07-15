import "./HrSidebar.css";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faHouseChimney} from "@fortawesome/free-solid-svg-icons";
import {faPerson} from "@fortawesome/free-solid-svg-icons";

interface HrSidebarProps {
    selectedContent: string;
    setSelectedContent: (content: string) => void;
}



export default function HrSidebar({ selectedContent, setSelectedContent }: HrSidebarProps) {

    return (
        <aside className="hr-sidebar-container">
            <button
                className={`hr-sidebar-button ${selectedContent === "HRHomeBody" ? "active" : ""}`}
                onClick={() => setSelectedContent('HRHomeBody')}
                type="button"
            >
                <FontAwesomeIcon icon={faHouseChimney} />
                <span>Ana Sayfa</span>
            </button>
            <button
                className={`hr-sidebar-button ${selectedContent === "HrAddNewPersonel" ? "active" : ""}`}
                onClick={() => setSelectedContent('HrAddNewPersonel')}
                type="button"
            >
                <FontAwesomeIcon icon={faPerson} />
                <span>Personel Ekle</span>
            </button>
        </aside>
    );
}