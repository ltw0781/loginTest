// import axios from 'axios';
import axios from 'axios';
import { useState } from 'react';
import { jwtDecode } from 'jwt-decode';
import { useNavigate } from 'react-router-dom';
import Cookies from 'js-cookie';
import { setUserFromToken } from '../store/usersSlice';
import { useDispatch } from 'react-redux';

function LoginPage() {

    const dispatch = useDispatch();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();

    const handleLogin = async (e) => {

        e.preventDefault();
        try {
            const res = await axios.post('http://localhost:8080/api/auth/login',{username, password}
                ,{withCredentials:true}
            );

            console.log(jwtDecode(res.data.accessToken));
            Cookies.set('accessToken',res.data.accessToken);
            dispatch(setUserFromToken(res.data.accessToken));

            alert("로그인 성공! Access Token : " + res.data.accessToken, { expires: 0.021,path: '/' });
            navigate('/');
        } catch (err) {
            alert("로그인 실패 : " + (err.response?.data || err.message));
        }

    }

    return (
        <div className='auth-container'>
            <h2>로그인</h2>
            <form onSubmit={handleLogin}>
                <input value={username} onChange={(e) => setUsername(e.target.value)} placeholder='아이디' />
                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder='비밀번호' />
                <button type="submit">로그인</button>
            </form>
        </div>
    )

}

export default LoginPage;