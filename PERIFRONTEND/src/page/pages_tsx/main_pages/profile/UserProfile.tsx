import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../../../../components/header/Header";
import Footer from "../../../../components/footer/Footer";
import UserProfileSidebar from "../../../../components/sidebar/UserProfileSidebar";
import "../Application/App.css";
import "./UserProfile.css";

type UserProfileResponse = {
    data: {
        id: string;
        firstName: string;
        lastName: string;
        phoneNumber: string;
        email: string;
        userCategories: string;
        imageUrl: string | null;
        homeAddress: string | null;
        iban: string | null;
        bankName: string | null;
        bankAccountNumber: string | null;
        bankAccountType: string | null;
        numberOfChildren: number | null;
        nationalId: string | null;
        bloodType: string | null;
        educationLevel: string | null;
        gender: string | null;
        maritalStatus: string | null;
    };
    code: number;
    message: string;
};

const BLOOD_TYPES = ["A_POSITIVE", "A_NEGATIVE", "B_POSITIVE", "B_NEGATIVE", "AB_POSITIVE", "AB_NEGATIVE", "O_POSITIVE", "O_NEGATIVE"];
const EDUCATION_LEVELS = ["PrimarySchool", "MiddleSchool", "HighSchool", "AssociateDegree", "BachelorDegree", "MasterDegree", "Doctorate", "Other"];
const GENDERS = ["MALE", "FEMALE", "OTHER"];
const MARITAL_STATUSES = ["Single", "Married", "Divorced", "Widowed", "Separated", "Other"];

export default function UserProfile() {
    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState<"basic" | "personal">("basic");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [email, setEmail] = useState("");
    const [userCategories, setUserCategories] = useState("");
    const [imageUrl, setImageUrl] = useState("");
    const [homeAddress, setHomeAddress] = useState("");
    const [iban, setIban] = useState("");
    const [bankName, setBankName] = useState("");
    const [bankAccountNumber, setBankAccountNumber] = useState("");
    const [bankAccountType, setBankAccountType] = useState("");
    const [numberOfChildren, setNumberOfChildren] = useState("");
    const [nationalId, setNationalId] = useState("");
    const [bloodType, setBloodType] = useState("");
    const [educationLevel, setEducationLevel] = useState("");
    const [gender, setGender] = useState("");
    const [maritalStatus, setMaritalStatus] = useState("");
    const [isLoading, setIsLoading] = useState(true);
    const [isSaving, setIsSaving] = useState(false);

    const formatEnumLabel = (value: string) => {
        if (!value) return "-";
        return value.replaceAll("_", " ");
    };

    useEffect(() => {
        const loadProfile = async () => {
            const token = localStorage.getItem("token");
            if (!token) {
                navigate("/signin");
                return;
            }

            try {
                const response = await fetch("http://localhost:9090/dev/v1/user/profile", {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });

                if (!response.ok) {
                    alert("Profil bilgisi alınamadı. Lütfen tekrar giriş yapın.");
                    navigate("/signin");
                    return;
                }

                const payload: UserProfileResponse = await response.json();
                setFirstName(payload.data.firstName ?? "");
                setLastName(payload.data.lastName ?? "");
                setPhoneNumber(payload.data.phoneNumber ?? "");
                setEmail(payload.data.email ?? "");
                setUserCategories(payload.data.userCategories ?? "");
                setImageUrl(payload.data.imageUrl ?? "");
                setHomeAddress(payload.data.homeAddress ?? "");
                setIban(payload.data.iban ?? "");
                setBankName(payload.data.bankName ?? "");
                setBankAccountNumber(payload.data.bankAccountNumber ?? "");
                setBankAccountType(payload.data.bankAccountType ?? "");
                setNumberOfChildren(payload.data.numberOfChildren?.toString() ?? "");
                setNationalId(payload.data.nationalId ?? "");
                setBloodType(payload.data.bloodType ?? "");
                setEducationLevel(payload.data.educationLevel ?? "");
                setGender(payload.data.gender ?? "");
                setMaritalStatus(payload.data.maritalStatus ?? "");
            } catch (error) {
                alert("Profil yükleme hatası: " + (error as Error).message);
            } finally {
                setIsLoading(false);
            }
        };

        loadProfile();
    }, [navigate]);

    const handleSave = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const token = localStorage.getItem("token");
        if (!token) {
            navigate("/signin");
            return;
        }

        setIsSaving(true);
        try {
            const response = await fetch("http://localhost:9090/dev/v1/user/profile", {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`
                },
                body: JSON.stringify({
                    firstName,
                    lastName,
                    phoneNumber,
                    imageUrl,
                    homeAddress,
                    iban,
                    bankName,
                    bankAccountNumber,
                    bankAccountType,
                    numberOfChildren: numberOfChildren === "" ? null : Number(numberOfChildren),
                    nationalId,
                    bloodType: bloodType || null,
                    educationLevel: educationLevel || null,
                    gender: gender || null,
                    maritalStatus: maritalStatus || null
                })
            });

            if (!response.ok) {
                const errorText = await response.text();
                alert("Profil güncellenemedi: " + errorText);
                return;
            }

            alert("Profil bilgileri güncellendi.");
        } catch (error) {
            alert("Profil güncelleme hatası: " + (error as Error).message);
        } finally {
            setIsSaving(false);
        }
    };

    if (isLoading) {
        return (
            <div className="app-layout">
                <Header />
                <div className="app-main">
                    <UserProfileSidebar activeTab={activeTab} setActiveTab={setActiveTab} />
                    <main className="content-area user-profile-content">
                        <div className="text-center">Profil yükleniyor...</div>
                    </main>
                </div>
                <Footer />
            </div>
        );
    }

    return (
        <div className="app-layout">
            <Header />
            <div className="app-main">
                <UserProfileSidebar activeTab={activeTab} setActiveTab={setActiveTab} />
                <main className="content-area user-profile-content">
                    <div className="user-profile-card">
                        <h2 className="mb-4">Kullanıcı Profili</h2>
                        <div className="user-id-card mb-4">
                            <div className="user-id-card-top">
                                <div className="user-id-avatar">
                                    {imageUrl ? (
                                        <img src={imageUrl} alt="Profil" />
                                    ) : (
                                        <span>{`${firstName.charAt(0)}${lastName.charAt(0)}`.trim() || "U"}</span>
                                    )}
                                </div>
                                <div>
                                    <div className="user-id-name">{`${firstName} ${lastName}`.trim() || "İsimsiz Kullanıcı"}</div>
                                    <div className="user-id-role">{formatEnumLabel(userCategories)}</div>
                                </div>
                            </div>
                            <div className="user-id-grid">
                                <div>
                                    <span>E-posta</span>
                                    <strong>{email || "-"}</strong>
                                </div>
                                <div>
                                    <span>Telefon</span>
                                    <strong>{phoneNumber || "-"}</strong>
                                </div>
                                <div>
                                    <span>Pozisyon</span>
                                    <strong>{formatEnumLabel(userCategories)}</strong>
                                </div>
                                <div>
                                    <span>Kan Grubu</span>
                                    <strong>{formatEnumLabel(bloodType)}</strong>
                                </div>
                                <div>
                                    <span>Cinsiyet</span>
                                    <strong>{formatEnumLabel(gender)}</strong>
                                </div>
                                <div>
                                    <span>Medeni Durum</span>
                                    <strong>{formatEnumLabel(maritalStatus)}</strong>
                                </div>
                                <div>
                                    <span>TC Kimlik No</span>
                                    <strong>{nationalId || "-"}</strong>
                                </div>
                            </div>
                        </div>
                        <form onSubmit={handleSave}>
                            {activeTab === "basic" && (
                                <>
                                    <div className="mb-3">
                                        <label className="form-label">Ad</label>
                                        <input
                                            className="form-control"
                                            value={firstName}
                                            onChange={(e) => setFirstName(e.target.value)}
                                            required
                                        />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Soyad</label>
                                        <input
                                            className="form-control"
                                            value={lastName}
                                            onChange={(e) => setLastName(e.target.value)}
                                            required
                                        />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Telefon</label>
                                        <input
                                            className="form-control"
                                            value={phoneNumber}
                                            onChange={(e) => setPhoneNumber(e.target.value)}
                                            required
                                        />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">E-posta</label>
                                        <input className="form-control" value={email} disabled />
                                    </div>
                                    <div className="mb-4">
                                        <label className="form-label">Rol</label>
                                        <input className="form-control" value={userCategories} disabled />
                                    </div>
                                </>
                            )}

                            {activeTab === "personal" && (
                                <>
                                    <div className="mb-3">
                                        <label className="form-label">Profil Fotoğraf URL</label>
                                        <input className="form-control" value={imageUrl} onChange={(e) => setImageUrl(e.target.value)} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Ev Adresi</label>
                                        <input className="form-control" value={homeAddress} onChange={(e) => setHomeAddress(e.target.value)} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">IBAN</label>
                                        <input className="form-control" value={iban} onChange={(e) => setIban(e.target.value)} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Banka Adı</label>
                                        <input className="form-control" value={bankName} onChange={(e) => setBankName(e.target.value)} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Banka Hesap Numarası</label>
                                        <input className="form-control" value={bankAccountNumber} onChange={(e) => setBankAccountNumber(e.target.value)} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Banka Hesap Türü</label>
                                        <input className="form-control" value={bankAccountType} onChange={(e) => setBankAccountType(e.target.value)} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Çocuk Sayısı</label>
                                        <input className="form-control" type="number" min={0} value={numberOfChildren} onChange={(e) => setNumberOfChildren(e.target.value)} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">TC Kimlik Numarası</label>
                                        <input className="form-control" value={nationalId} onChange={(e) => setNationalId(e.target.value)} />
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Kan Grubu</label>
                                        <select className="form-control" value={bloodType} onChange={(e) => setBloodType(e.target.value)}>
                                            <option value="">Seçiniz</option>
                                            {BLOOD_TYPES.map((item) => (
                                                <option key={item} value={item}>{item}</option>
                                            ))}
                                        </select>
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Eğitim Seviyesi</label>
                                        <select className="form-control" value={educationLevel} onChange={(e) => setEducationLevel(e.target.value)}>
                                            <option value="">Seçiniz</option>
                                            {EDUCATION_LEVELS.map((item) => (
                                                <option key={item} value={item}>{item}</option>
                                            ))}
                                        </select>
                                    </div>
                                    <div className="mb-3">
                                        <label className="form-label">Cinsiyet</label>
                                        <select className="form-control" value={gender} onChange={(e) => setGender(e.target.value)}>
                                            <option value="">Seçiniz</option>
                                            {GENDERS.map((item) => (
                                                <option key={item} value={item}>{item}</option>
                                            ))}
                                        </select>
                                    </div>
                                    <div className="mb-4">
                                        <label className="form-label">Medeni Durum</label>
                                        <select className="form-control" value={maritalStatus} onChange={(e) => setMaritalStatus(e.target.value)}>
                                            <option value="">Seçiniz</option>
                                            {MARITAL_STATUSES.map((item) => (
                                                <option key={item} value={item}>{item}</option>
                                            ))}
                                        </select>
                                    </div>
                                </>
                            )}
                            <button type="submit" className="btn btn-primary" disabled={isSaving}>
                                {isSaving ? "Kaydediliyor..." : "Profili Kaydet"}
                            </button>
                        </form>
                    </div>
                </main>
            </div>
            <Footer />
        </div>
    );
}
