import { BrowserRouter as Router, Routes, Route } from "react-router-dom"
import Home from "./pages/Home"
import LoginPage from "./pages/LoginPage"
import SignupPage from "./pages/SignupPage"
import AdminPage from "./pages/AdminPage"
import Navbar from "./components/Navbar"
import './css/App.css'
import { useDispatch } from "react-redux"
import { useEffect } from "react"
import Cookies from "js-cookie";
import { setUserFromToken } from "./store/usersSlice"

function App() {

  const dispatch = useDispatch();

  useEffect( () => {
    const token = Cookies.get("accessToken");
    if(token) {
      dispatch(setUserFromToken(token));
    }
  }, [dispatch]);

  return (
    <Router>
      <Navbar />
      <div style={{ paddingTop: '80px' }}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route path="login" element={<LoginPage />} />
          <Route path="admin" element={<AdminPage />} />
        </Routes>
      </div>
    </Router>
  )
}

export default App
