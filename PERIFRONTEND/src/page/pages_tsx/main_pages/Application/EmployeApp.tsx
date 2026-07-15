




import "./App.css"
import Header from "../../../../components/header/Header.tsx";
import Footer from "../../../../components/footer/Footer.tsx";
import EmployeeHomeBody from "../../../../components/body/EmployeeHomeBody.tsx";











export default function EmployeApp() {
    return (
        <div className="app-layout">
            <Header />
            <div className="app-main">

                <main className="content-area">
                    <EmployeeHomeBody/>
                </main>
            </div>
            <Footer />
        </div>
    );
}
