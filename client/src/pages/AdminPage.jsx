import { useEffect, useState } from "react";
import axiosInstance from "../api/axiosInstance";
import { useNavigate } from "react-router-dom";

const AdminPage = () => {

    const [users, setUsers] = useState([]);
    const navigate = useNavigate();

    const fetchUsers = async () => {

        try {
            const res = await axiosInstance.get('/api/admin/users');
            setUsers(res.data);
        } catch (err) {
            alert("사용자 정보를 불러오는데 실패했습니다.");
            navigate('/');
        }

    }

    useEffect(()=> {
        fetchUsers();
    }, []);

    return (
        <div className="home">
            Admin 페이지 입니다.
            <ul>
                {users.map((user) => (
                    <li key={user.id}>
                        {user.username} ({user.role})
                    </li>
                ))}
            </ul>
        </div>
    )
}

export default AdminPage;