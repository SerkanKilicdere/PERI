import { useState} from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import * as React from "react";
import { API_BASE_URL } from "../../../../tools/api";

export default function RegisterCompany() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [searchParams] = useSearchParams();
    const token = searchParams.get("token");
    const navigate = useNavigate();




    // 3. ADIM: Her şey yolundaysa formu göster
    const handleRegister = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (password !== confirmPassword) {
            alert("Şifreler eşleşmiyor!");
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/dev/v1/systemadministrator/register`, {
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