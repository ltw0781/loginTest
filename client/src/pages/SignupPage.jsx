import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function SignupPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const navigate = useNavigate();

    const handleSignup = async (e) => {



    }

    return (
        <div className='auth-container'>
            <h2>회원가입</h2>
            <form onSubmit={handleSignup}>
                <input
                    type="text"
                    placeholder="아이디"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="비밀번호"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <button type="submit">회원가입</button>
            </form>
        </div>
    )

}

export default SignupPage;