import { useEffect, useState } from "react";

export default function Products(props: { updateCart: any }) {
  const [selectedCategory, setSelectedCategory] = useState("All");
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);

  const fetchProducts = async () => {
    const res = await fetch("http://localhost:8080/api/products", {
      credentials: "include",
    });
    const products = await res.json();
    setProducts(products);
  };

  const fetchCategories = async () => {
    const res = await fetch("http://localhost:8080/api/categories", {
      credentials: "include",
    });
    const categories = await res.json();
    setCategories(categories);
  };

  useEffect(() => {
    fetchProducts();
    fetchCategories();
  }, []);

  async function addToCart(productId: number) {
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
    props.updateCart(await res.json());
  }
  // const {
  //   isPending: isProductsPending,
  //   error: pErrors,
  //   data: products,
  // } = useQuery({
  //   queryKey: ["productData"],
  //   queryFn: () =>
  //     fetch("http://localhost:8080/api/products").then((res) => res.json()),
  // });

  // const {
  //   isPending: isCategoriesPending,
  //   error: cErrors,
  //   data: categories,
  // } = useQuery({
  //   queryKey: ["categoriesData"],
  //   queryFn: () =>
  //     fetch("http://localhost:8080/api/categories").then((res) => res.json()),
  // });

  const cateogoryOptions = categories.map((category: any) => (
    <option key={category.id} value={category.title}>
      {category.title}
    </option>
  ));
  cateogoryOptions.unshift(
    <option key="All" value="All">
      All
    </option>,
  );

  const filteredProducts =
    selectedCategory !== "All"
      ? products.filter(
          (product: any) => product.productCategory.title === selectedCategory,
        )
      : products;

  const productsComponents = filteredProducts.map((product: any) => (
    <div style={{ padding: "0px 0px 15px 15px" }} key={product.id}>
      <h3>Name: {product.title}</h3>
      <p>Price: {product.price}</p>
      <img src={product.image}></img>
      <button onClick={() => addToCart(product.id)}>Add to cart</button>
    </div>
  ));

  return (
    <>
      <h1 style={{ display: "inline", marginRight: "15px" }}>Products</h1>
      <select
        style={{ display: "inline" }}
        value={selectedCategory}
        onChange={(e) => setSelectedCategory(e.target.value)}
      >
        {cateogoryOptions}
      </select>
      {productsComponents}
    </>
  );
}
