function makeMultiplier(factor) {
    return function(num) {
        return num * factor;
    }
}

let double = makeMultiplier(2);
console.log("Closure example in JavaScript:", double(5));
