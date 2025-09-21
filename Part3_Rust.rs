fn main() {
    let s = String::from("Hello");
    print_string(&s); // borrowing reference
    // ownership rules ensure memory is freed automatically
}

fn print_string(s: &String) {
    println!("Rust string: {}", s);
}
