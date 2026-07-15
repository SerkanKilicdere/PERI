import { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import * as React from "react";

export default function RegisterUser() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [searchParams] = useSearchParams();
    const token = searchParams.get("token");
    const navigate = useNavigate();

    // Yeni eklenen durumlar: Token kontrolünün bitip bitmediğini izler
    const [isLoading, setIsLoading] = useState(true);
    const [hasTokenError, setHasTokenError] = useState(false);

    useEffect(() => {
        // React Router'a URL'i ayrıştırması için ufak bir esneklik tanıyoruz
        if (token) {
            console.log("URL'den Token Başarıyla Okundu:", token);
            setHasTokenError(false);
            setIsLoading(false);
        } else {
            // İlk renderda hemen hata fırlatmamak için kısa bir süre bekletip emin oluyoruz
            const timeout = setTimeout(() => {
                if (!searchParams.get("token")) {
                    console.log("Token bulunamadı, hata ekranına geçiliyor.");
                    setHasTokenError(true);
                    setIsLoading(false);
                }
            }, 300); // 300 milisaniye bekleme payı

            return () => clearTimeout(timeout);
        }
    }, [token, searchParams]);

    // 1. ADIM: URL okunurken kısa bir yükleniyor ekranı göster
    if (isLoading) {
        return (
            <div className="signin-wrapper text-center m-5">
                <div className="spinner-border text-primary" role="status">
                    <span className="visually-hidden">Yükleniyor...</span>
                </div>
                <p className="mt-2">Doğrulama kontrol ediliyor...</p>
            </div>
        );
    }

    // 2. ADIM: Süre dolmasına rağmen token gelmediyse Kırmızı Kutuyu göster
    if (hasTokenError) {
        return (
            <div className="signin-wrapper">
                <div className="alert alert-danger text-center m-5" role="alert">
                    <h4>Doğrulama token'ı bulunamadı!</h4>
                    <p>Lütfen e-postanıza gelen linkin eksiksiz ve doğru olduğundan emin olun.</p>
                </div>
            </div>
        );
    }

    // 3. ADIM: Her şey yolundaysa formu göster
    const handleRegister = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (password !== confirmPassword) {
            alert("Şifreler eşleşmiyor!");
            return;
        }

        try {
            const response = await fetch("http://localhost:9090/dev/v1/humanresource/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    token: token,
                    emailAddress: email,
                    password: password,
                    rePassword: confirmPassword
                })
            });

            if (!response.ok) {
                const errorData = await response.text();
                alert("Hata: " + errorData);
                return;
            }

            alert("Kayıt başarılı! Giriş sayfasına yönlendiriliyorsunuz.");
            navigate("/signin");

        } catch (error) {
            alert("Bir hata oluştu: " + (error as Error).message);
        }
    };

    return (
        <div className="signin-wrapper">
            <div className="signin-box">
                <h2 className="signin-title">Şifrenizi Belirleyin</h2>
                <form onSubmit={handleRegister}>
                    <div className="mb-3">
                        <label className="form-label">Email</label>
                        <input
                            type="email"
                            className="form-control"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Yeni Şifre</label>
                        <input
                            type="password"
                            className="form-control"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label className="form-label">Şifre Tekrar</label>
                        <input
                            type="password"
                            className="form-control"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className="btn btn-primary mt-2">
                        Hesabımı Oluştur
                    </button>
                </form>
            </div>
        </div>
    );
}