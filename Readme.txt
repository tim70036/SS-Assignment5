103062121   劉亮廷     assignment5

    我使用上一次作業的code，其中更動的部份有Typing以及新增一個Server class。

Typing:


    增加了一些需要的 Atrribute，在run()裡面，一開始會有一個迴圈等待server說開始，
    在此迴圈中會一直read server傳來的訊息，當訊息確定是開始遊戲，會建立執行GameStage 的
    Thread，設定TextField是可輸入的並跳出迴圈。之後會再建立一個Thread來執行一個迴圈不斷的
    接收來自server的訊息。最後是進入遊戲的迴圈，不斷的更改圖片的y座標，並檢查遊戲是否結束。


    而TextField的ActionListener也做了一些更動，現在當使用者輸入答案時會將訊息傳至Server，
    並將TextField設為不可輸入。


    最後為了顯示等待的訊息,錯誤答案的訊息，我使用JLabel以及一個drawMode的整數來操作，當
    drawMode == 0 會顯示waiting的JLabel(等待其他玩家連線或使用者輸入答案等待server回應
    時)，當drawMode == 1會顯示字的圖片(遊戲進行中)，當drawMode == 2會顯示wrong的JLabel
    (server回傳的訊息表示答案錯誤)，並在paintComponent()中依據drawMode來繪圖。


    而接收server訊息的迴圈中，會先read一個字串，根據這個字串才能知道server要client做什麼
    動作，若這個字串是"changePicture"代表，答案正確要換下一張圖片了，這時候會再讀一個
    字串，這個字串就會是圖案的檔名，並將drawMode設為1。


    若server傳來的訊息是"Wrong answer"，代表剛剛輸入的答案有錯，這時會執行答錯的動畫並將
    drawMode設為2以顯示wrong的label，並維持一段時間後(sleep)再將drawMode改回1，並將
    TextField改為可以輸入。


    若server傳來的訊息是"Right answer"，代表答案正確，執行correct()更改分數,並執行答對
    動畫，將TextField改為可以輸入。


Server:


    我做的server可以超過兩個人連線，而檢查答案也會檢查全部人的答案。
    Server本身繼承JFrame，其中有textArea，會顯示server目前的狀態，並在上面使用了scrollPane。


    runServer()中，Socket會在迴圈中不斷的接受連線，並將連線後建立的ConnectionThread放入
    connectionPool裡面。當connectionPool的size >= 2時，代表遊戲可以開始了，此時會廣播開
    始遊戲的訊息給所有client。若之後有其他人加入，也一樣會廣播給所有人。


    ConnectionThread是一個class extends Thread，裡面有連接client的socket,PrintWriter
    ,BufferedReader。在run()之中會不斷的接收client的訊息，當client傳入答案時會將之放入answer
    中(ArrayList)，當answer的數量跟connectionPool的數量一樣多時，代表著所有連線了人都輸入
    了答案，這時就會檢查answer中的字串是不是都一樣。如果是的話就是答對執行correct(true)否則
    執行correct(false)。


    correct()，會依據傳入的參數，若是true代表答案正確，將這個word設為已使用過，並執行
    changePicture()，最後廣播答對的訊息給所有client。如果是false代表答案錯誤，廣播錯誤訊
    息給所有client。


    changePicture()，找到一個未使用過的字的檔名，通知所有client要更改圖片，廣播檔名給所有
    的client。


遇到的困難：

    原本以為client要在開另外一個class來處理Socket，但後來發現這樣會太麻煩，所以就直接寫在
    Typing裡。

    一開始我就另外開一個Thread來接收server訊息，後來發現這樣會造成跟Typing的run()
    不同步，可能已經收到遊戲開始的訊息，但Typing的Constructor都還沒跑完，所以後來改成把接收
    server訊息的Thread寫在run裡面，如此就可以確定Typing的東西都建立完成。

    scrollPane的捲軸我原本無法顯示出來，後來使用setPreferredSize以後才出現。
