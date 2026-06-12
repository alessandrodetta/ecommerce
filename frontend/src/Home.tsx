import { useEffect, useState } from "react";
import Cart from "./Cart";
import Products from "./Products";

export default function Home() {
  const [cart, setCart] = useState<any>({});
  const [username, setUsername] = useState<string>("");
  const [password, setPassword] = useState<string>("");

  const fetchCart = async () => {
    const res = await fetch("http://localhost:8080/api/cart", {
      credentials: "include",
    });
    if (res.ok) {
      const cart = await res.json();
      setCart(cart);
      console.log("Cart fetched successfully", res.status);
    } else {
      console.log("Unable to fetch the cart", res.status);
    }
  };

  const updateCart = (updatedCart: any) => {
    setCart(updatedCart);
  };

  const handleRegister = async () => {
    const res = await fetch("http://localhost:8080/api/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: username,
        password: password,
      }),
      credentials: "include",
    });
    if (res.ok) {
      console.log("Registration successful.");
      fetchCart();
    } else {
      console.error("Registration error:", res.status);
    }
  };

  const handleLogin = async () => {
    const res = await fetch("http://localhost:8080/api/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: username,
        password: password,
      }),
      credentials: "include",
    });
    if (res.ok) {
      console.log("Login successful.");
      fetchCart();
    } else {
      console.error("Login error:", res.status);
    }
  };

  const handleLogout = async function () {
    const res = await fetch("http://localhost:8080/logout", {
      method: "POST",
      credentials: "include",
    });

    if (res.ok) {
      setUsername("");
      setPassword("");
      console.log("Logout successful");
      fetchCart();
    } else {
      console.error("Logout error:", res.status, res.statusText);
    }
  };

  useEffect(() => {
    fetchCart();
  }, []);

  return (
    <div>
      {/* Sezione Autenticazione */}
      <div
        style={{
          marginTop: "25px",
          padding: "15px",
          border: "1px solid #ccc",
          borderRadius: "5px",
        }}
      >
        <h3>User authentication</h3>
        <label htmlFor="email">Email: </label>
        <input
          id="email"
          type="text"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <br />
        <br />
        <label htmlFor="password">Password: </label>
        <input
          id="password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <br />
        <br />

        {/* Pulsanti di autenticazione */}
        <button onClick={handleRegister}>Register</button>
        <button onClick={handleLogin} style={{ marginLeft: "5px" }}>
          Login
        </button>

        {/* NUOVO: Pulsante di Logout inserito qui */}
        <button onClick={handleLogout} style={{ marginLeft: "5px" }}>
          Logout
        </button>
      </div>

      {/* Intestazione riepilogo */}
      <h4 style={{ marginTop: "20px", marginBottom: "20px" }}>
        CartID: {cart.id || "Not set"} | Total amount: {cart.totalAmount || 0}
      </h4>

      {/* Layout a due colonne */}
      <div style={{ display: "flex", gap: "30px", alignItems: "flex-start" }}>
        {/* Prima colonna: Sezione Prodotti */}
        <div style={{ flex: 2, minWidth: "300px" }}>
          <Products updateCart={updateCart} />
        </div>

        {/* Seconda colonna: Sezione Carrello */}
        <div
          style={{
            flex: 1,
            minWidth: "250px",
            backgroundColor: "#f9f9f9",
            padding: "20px",
            borderRadius: "5px",
            border: "1px solid #eee",
          }}
        >
          <Cart cart={cart} />
        </div>
      </div>
    </div>
  );
}
