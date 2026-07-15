
import Header from "../../../../components/header/Header";
import Footer from "../../../../components/footer/Footer";
import "./App.css";
import HrSidebar from "../../../../components/sidebar/HrSidebar";
import { useState } from "react";
import HrAddNewPersonel from "../../../../components/body/HrAddNewPersonel.tsx";
import HRHomeBody from "../../../../components/body/HRHomeBody";



export default function HumanResourceApp() {
    const [selectedContent, setSelectedContent] = useState<string>('HRHomeBody');

    const renderContent = () => {
        switch (selectedContent) {
            case 'HRHomeBody':
                return <HRHomeBody />;
            case 'HrAddNewPersonel':
                return <HrAddNewPersonel />;
            default:
                return <div>Select a menu item</div>;
        }
    };



    return (
        <div className="app-layout">
            <Header />
            <div className="app-main">
                <HrSidebar selectedContent={selectedContent} setSelectedContent={setSelectedContent}/>
                <main className="content-area"> {/* İçerik burada izole edilir */}
                    {renderContent()}
                </main>
            </div>
            <Footer />
        </div>
    );
}
