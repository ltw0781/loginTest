import { Link, useLocation, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import Cookies from "js-cookie";
import axiosInstance from "./../api/axiosInstance";
import { logout } from "../store/usersSlice";

const Navbar = () => {

    const location = useLocation();
    const { role, isAuthenticated } = useSelector((state) => state.user);

    const navigate = useNavigate();
    const dispatch = useDispatch();

    const handleLogout = async () => {
        try {
            await axiosInstance.post("/api/auth/logout");
        } catch (err) {
            console.error("서버 로그아웃 실패! : " , err);
        }

        Cookies.remove("accessToken");
        dispatch(logout());

        navigate("/");
    };

    return (
        <nav className="navbar">
            <div className='navbar-logo'>MyApp</div>
            <div className='navbar-links'>
                <Link className={location.pathname === "/" ? "active" : ""} to="/">홈</Link>
                {isAuthenticated ? (
                    <>
                        {role === 'ROLE_ADMIN' && <Link className={location.pathname === "/admin" ? "active" : ""} to="/admin">관리자</Link>}
                        <Link onClick={handleLogout}>로그아웃</Link>
                    </>
                ) : (
                    <>
                        <Link className={location.pathname === "/login" ? "active" : ""} to="/login">로그인</Link>
                        <Link className={location.pathname === "/signup" ? "active" : ""} to="/signup">회원가입</Link>
                    </>
                )}
            </div>
        </nav>
    )

}

export default Navbar;