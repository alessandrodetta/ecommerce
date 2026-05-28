export default function Cart(props: { cart: any }) {
  if (props.cart.items != null) {
    let totalPrice = 0;
    const items = props.cart.items.map((item: any) => {
      totalPrice += item.quantity * item.product.price;
      return (
        <div style={{ padding: "0px 0px 15px 15px" }} key={item.product.id}>
          <h3>Name: {item.product.title}</h3>
          <h5>Descrizione: {item.product.description}</h5>
          <p>Quantità: {item.quantity}</p>
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
