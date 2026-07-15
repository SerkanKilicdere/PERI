import {BrowserRouter, Route, Routes} from "react-router";
import HumanResourceApp from "./page/pages_tsx/main_pages/Application/HumanResourceApp.tsx";
import SignIn from "./page/pages_tsx/main_pages/signin/SignIn.tsx";
import RegisterUser from "./page/pages_tsx/main_pages/register/RegisterUser.tsx";
import Landing from "./page/pages_tsx/main_pages/Landing/Landing.tsx";
import EmployeApp from "./page/pages_tsx/main_pages/Application/EmployeApp.tsx";
import Purchase from "./page/pages_tsx/main_pages/purchase/Purchase.tsx";
import RegisterCompany from "./page/pages_tsx/main_pages/register/RegisterCompany.tsx";
import UserProfile from "./page/pages_tsx/main_pages/profile/UserProfile.tsx";
import ContactMessages from "./page/pages_tsx/main_pages/admin/ContactMessages.tsx";
import QuoteRequests from "./page/pages_tsx/main_pages/admin/QuoteRequests.tsx";

const Router =()=> {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Landing/>}/>
                <Route path="/signin" element={<SignIn/>}/>
                <Route path="/register" element={<RegisterUser/>}/>
                <Route path="/humanresource" element={<HumanResourceApp/>}/>
                <Route path="/employeeapp" element={<EmployeApp/>}/>
                <Route path="/app" element={<HumanResourceApp/>}/>
                <Route path="/SatinAl" element={<Purchase />} />
                <Route path="/registercompany" element={<RegisterCompany/>}/>
                <Route path="/userprofile" element={<UserProfile/>}/>
                <Route path="/admin/messages" element={<ContactMessages/>}/>
                <Route path="/admin/quotes" element={<QuoteRequests/>}/>
            </Routes>
        </BrowserRouter>
    )
}

export default Router;
