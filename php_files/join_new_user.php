<?php

$connection = new mysqli("localhost", "root", "", "onlie_store_db");

$del = $connection->prepare("insert into products values (?,?,?");
$del->bind_param("isiss", $_GET["id"], $_GET["name"], $_GET["price"], $_GET["brand"], $_GET["picture"]);
$del->execute();
// If an email already exists, do not add the user again
$emailCheckSQLCommand = $connection->prepare("select * from app_users_table where email=?");
$emailCheckSQLCommand->bind_param("s", $_GET["email"]);
$emailCheckSQLCommand->execute();
$emailResult = $emailCheckSQLCommand->get_result();

if ($emailResult->num_rows == 0) {
    
    $sqlCommand = $connection->prepare("insert into app_users_table values (?, ?, ?)");
$sqlCommand->bind_param("sss", $_GET["email"], $_GET["username"], $_GET["pass"]);
$sqlCommand->execute();
echo 'Congratulations! The registration process was successful';

} else {
    echo 'A user with this Email Address already exists';
}
