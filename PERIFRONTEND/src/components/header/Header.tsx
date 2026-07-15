import "./Header.css";

export default function Header() {
    return (
        <header className="header-container">
            <div className="logo"><img alt="Peri" src="../../public/img/perilogo.png"/>PERİ</div>
            <nav className="header-nav">
                <button className="btn btn-primary">Sign Out</button>

            </nav>
        </header>
    );
}
