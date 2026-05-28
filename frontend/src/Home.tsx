import { useEffect, useState } from "react";
import Cart from "./Cart";
import Products from "./Products";

export default function Home() {
  const [cart, setCart] = useState<any>({});
  const [emailInput, setEmailInput] = useState<any>({});
  const [passwordInput, setPasswordInput] = useState<any>({});

  const fetchCart = async function () {
    const res = await fetch("http://localhost:8080/api/cart", {
      credentials: "include",
    });
    const cart = await res.json();
    setCart(cart);
  };
  const fetchRegister = async function () {
    console.log(emailInput.value);
    const res = await fetch("http://localhost:8080/api/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: emailInput.target.value,
        password: passwordInput.target.value,
      }),
      credentials: "include",
    });
    console.log(res.status);
  };
  const fetchLogin = async function () {
    console.log(emailInput.value);
    const res = await fetch("http://localhost:8080/api/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: emailInput.target.value,
        password: passwordInput.target.value,
      }),
      credentials: "include",
    });
    console.log(res.status);
  };
  useEffect(() => {
    fetchCart();
  }, []);
  return (
    <div>
      <h4>
        UserID: {document.cookie}, CartID: {cart.id}
      </h4>
      <Products updateCart={updateCart} />
      <Cart cart={cart} />
      <label htmlFor="email">Email</label>
      <input id="email" type="text" onChange={emailChange} />
      <label htmlFor="password">Password</label>
      <input id="password" type="password" onChange={passwordChange} />
      <button onClick={register}>Register</button>
      <button onClick={login}>Login</button>
    </div>
  );

  function emailChange(e: any) {
    setEmailInput(e);
  }

  function passwordChange(e: any) {
    setPasswordInput(e);
  }
  function register() {
    fetchRegister();
  }
  function login() {
    fetchLogin();
  }
  function updateCart(cart: any) {
    setCart(cart);
  }
}
