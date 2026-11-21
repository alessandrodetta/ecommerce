import { useEffect, useState } from "react";

export default function Products(props: { updateCart: any }) {
  const [selectedCategory, setSelectedCategory] = useState("All");
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);

  // CORREZIONE: Inseriti blocchi try-catch e verifiche res.ok per bloccare fallimenti silenziosi in rete
  const fetchProducts = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/products", {
        credentials: "include",
      });
      if (!res.ok) throw new Error(`Errore di rete: ${res.status}`);
      const data = await res.json();
      setProducts(data);
    } catch (error) {
      console.error("Impossibile caricare la lista prodotti:", error);
    }
  };

  const fetchCategories = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/categories", {
        credentials: "include",
      });
      if (!res.ok) throw new Error(`Errore di rete: ${res.status}`);
      const data = await res.json();
      setCategories(data);
    } catch (error) {
      console.error("Impossibile caricare le categorie:", error);
    }
  };

  useEffect(() => {
    fetchProducts();
    fetchCategories();
  }, []);

  async function addToCart(productId: number) {
    try {
      const requestBody = {
        productId: productId,
        quantity: 1,
      };
      const res = await fetch("http://localhost:8080/api/cart/items", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestBody),
        credentials: "include",
      });
      if (!res.ok)
        throw new Error("Risposta server non valida durante l'aggiunta");
      props.updateCart(await res.json());
    } catch (error) {
      console.error("Errore aggiunta al carrello:", error);
    }
  }

  const ts = 2;

  const categoryOptions = categories.map((category: any) => (
    <option key={category.id} value={category.title}>
      {category.title}
    </option>
  ));

  categoryOptions.unshift(
    <option key="All" value="All">
      All
    </option>,
  );

  const filteredProducts =
    selectedCategory !== "All"
      ? products.filter(
          // CORREZIONE: Aggiunto l'operatore di navigazione sicura (?.) per prevenire crash se productCategory fosse null
          (product: any) => product.productCategory?.title === selectedCategory,
        )
      : products;

  const productsComponents = filteredProducts.map((product: any) => (
    <div
      style={{
        padding: "0px 0px 15px 15px",
        borderBottom: "1px solid #fafafa",
      }}
      key={product.id}
    >
      <h3>Name: {product.title}</h3>
      <p>Price: {product.price}</p>
      {product.image && (
        <img
          src={product.image}
          alt={product.title}
          style={{ maxWidth: "100px" }}
        />
      )}
      <br />
      <button onClick={() => addToCart(product.id)}>Add to Cart</button>
    </div>
  ));

  return (
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
      <h1>Products</h1>
      <label htmlFor="category-select">Filter by category: </label>
      <select
        id="category-select"
        value={selectedCategory}
        onChange={(e) => setSelectedCategory(e.target.value)}
      >
        {categoryOptions}
      </select>
      <div style={{ marginTop: "15px" }}>
        {productsComponents.length > 0 ? (
          productsComponents
        ) : (
          <p>Nessun prodotto disponibile.</p>
        )}
      </div>
    </div>
  );
}
