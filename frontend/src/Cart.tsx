export default function Cart(props: { cart: any }) {
  function handleDecrease() {}
  function handleIncrease() {}
  if (
    props.cart != null &&
    props.cart.items != null &&
    props.cart.items.length > 0
  ) {
    let totalPrice = 0;
    const items = props.cart.items.map((item: any) => {
      totalPrice += item.quantity * item.product.price;
      return (
        <div style={{ padding: "0px 0px 15px 15px" }} key={item.product.id}>
          <h3>Name: {item.product.title}</h3>
          <h5>Descrizione: {item.product.description}</h5>
          <p style={{ display: "inline", marginRight: "5px" }}>
            Quantità: {item.quantity}
          </p>
          <button
            onClick={handleDecrease}
            style={{ display: "inline", marginRight: "5px" }}
          >
            -
          </button>
          <button onClick={handleIncrease}>+</button>
          <p>Prezzo: {item.totalAmount}</p>
        </div>
      );
    });
    return (
      <>
        <h1>Cart</h1>
        {items}
        <h3>Total price: {props.cart.totalAmount}</h3>
      </>
    );
  } else {
    return (
      <>
        <h1>Cart</h1>
        <div>Nessun prodotto è stato aggiunto al carrello</div>
      </>
    );
  }
}
