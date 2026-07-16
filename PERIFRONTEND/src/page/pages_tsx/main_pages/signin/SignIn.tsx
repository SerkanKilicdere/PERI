import { useState } from "react";
import "./SignIn.css";
import * as React from "react";
import {useNavigate} from "react-router-dom";
import { API_BASE_URL } from "../../../../tools/api";


export default function SignIn() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();


    const signIn = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        try {
            const response = await fetch(`${API_BASE_URL}/dev/v1/signin/login`, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({ emailAddress: email, password: password })
            });

            if (!response.ok) {
                alert("Login failed!");
                return;
            }

            const data = await response.json();
            let token = data.data;
            token = token.replace("Bearer ", "");

            localStorage.setItem("token", token);



            const payload = JSON.parse(atob(token.split('.')[1]));
            console.log("Token Payload içeriği:", payload); // Bunu mutlaka kontrol et
/*

            let roles = payload.role || [];

            if (typeof roles === "string") {
                roles = [roles];
            }


            if (roles.includes("OWNER_USER")) {
                navigate("/userprofile");
            } else {
                alert("Yetkisiz rol. Token içindeki rolünüz: " + roles);
            }
*/
            navigate("/userprofile");

        } catch (error) {
            alert("Login error: " + (error as Error).message);
        }

    };
    return (
        <div className="signin-wrapper">
            <div className="signin-box">
                <h2 className="signin-title">Giriş Yap</h2>

                <form onSubmit={signIn}>
                    <div className="mb-3">
                        <label className="form-label">Email Adresi</label>
                        <input
                            type="email"
                            name="username"
                            className="form-control"
                            placeholder="ornek@firma.com"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                    </div>

                    <div className="mb-3">
                        <label className="form-label">Şifre</label>
                        <input
                            type="password"
                            name="password"
                            className="form-control"
                            placeholder="••••••••"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    <button  type="submit" className="btn btn-primary mt-2">
                        Giriş Yap
                    </button>
                </form>


            </div>
        </div>
    );
}
