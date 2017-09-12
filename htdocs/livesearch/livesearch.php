<?php

  require_once('config.php');
  
  //checking queryString is empty or not 
  if(isset($_POST["queryString"])) {
      $characters = $_POST["queryString"];
	  // We are searching the database and finding any values that have queryString in any position
      $characters = "%" . $characters . "%";
      $statement = $connection->prepare(
          "SELECT * FROM fish WHERE fishname LIKE :fishname ORDER BY ID LIMIT 5");
      $statement->bindParam(':fishname', $characters, PDO::PARAM_STR);
      $statement->execute();
      if ($statement->rowCount()>0) {
          $row_all = $statement->fetchall(PDO::FETCH_ASSOC);
          header('Content-type: application/json');
          echo json_encode($row_all);
      }
      else
        echo "No records found.";
  }
  else
  {
    echo "Something wrong with post";
  }
?>
