<?php

$connection = new mysqli("localhost", "root", "", "onlie_store_db");
$sqlCommand = $connection->prepare("delete from temporary_place_order where email=?");
$sqlCommand->bind_param("s", $_GET["email"]);
$sqlCommand->execute();

