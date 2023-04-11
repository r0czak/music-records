import React, { useState} from "react";

export const Register = (props) => {
    
    const [email, setEmail] = useState('');
    const [pass, setPass] = useState('');
    const [name, setName] = useState('');


    const handleSubmit = (event) => {
        event.preventDefault();
        console.log(email);
    }


    return (
        <div className="auth-form-container">
            <h2>Register</h2>
            <form className="register-form" onSubmit={handleSubmit}>
                <label htmlFor="name">Full Name: </label>
                <input value={name} onChange={(event) => setName(event.target.value)} type="text"  name="name" id="name" placeholder="Full Name" />
                <label htmlFor="email">Email: </label>
                <input value={email} onChange={(event) => setEmail(event.target.value)} type="email" id="email" placeholder="youremail@gmail.com" name="email" />
                <label htmlFor="password">Password: </label>
                <input value={pass} onChange={(event) => setPass(event.target.value)} type="password" id="password" name="password" />
                <button type="submit">Log In</button>
            </form>
            <button className="link-btn" onClick={() => props.onFormSwitch('login')}>Already have an account? Login here.</button>
        </div>
    )
}