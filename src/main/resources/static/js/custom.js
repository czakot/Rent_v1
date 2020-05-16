/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var count;
var redirect;

function countDown(c,r) {
count = c; // Timer
redirect = r; // Target URL
countDownWorker();
}

function countDownWorker() {
    if (count < 0) {
        window.location.href = redirect;
    } else {
        document.getElementById("timer").innerHTML = count--;
        setTimeout("countDownWorker()", 1000);
    }
}