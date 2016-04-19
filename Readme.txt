Main:
    第一部分Human OCR的程式進入點，創建MyWindow物件。


MyWindow:
    繼承JFrame，為遊戲的主體，其中有GameStage, Typing兩個panel，並在一個無窮迴圈中，
    不斷的初始化GameStage, Typing，並建立兩個Thread去執行他們兩個，直到這兩個Thread
    結束後，會跳出訊息框詢問是否重玩，如果不要重玩的話會跳出迴圈。


GameStage:
    為遊戲右半邊的畫面，主要工作為顯示動畫及記錄分數，在建構式讀入各個物件(Duck,Ball....)
    的Image，並建立JLabel顯示分數，init()初始化Attribute，paintComponent()
    依據各個物件的x,y資料畫出圖形，addScore()增加分數並更新JLabel。

    最重要的run()是遊戲執行的過程，由一個while迴圈不斷檢查stop是否為true，true的話代表遊
    戲結束了就跳出迴圈。迴圈中，第一個是讓Duck上下漂浮而依據Duck的方向加減Duck的y座標。
    第二個是當分數有變動時，依據得分決定要將BG,Ball往後移或Duck往前移，並開一個Thread來執
    行這個動畫，最後則是檢查是否贏了(21分)，贏了就將stop設成true，下一次迴圈就不會跑了。


Typing:
    為遊戲左半邊玩家輸入的畫面，主要工作為顯示動畫及讀取玩家輸入並判斷。建構式讀入known,
    unknown word的檔案，將known的資料存在一個ArrayList<Word>，unknown存在ArrayList
    <String>，並建立JTextField，為他建立一個ActionListener，當使用者輸入input時，會檢查
    是否正確(第一個known word字是否正確以及第二個字不能沒有輸入)，正確就執行correct()，其
    他則執行changeBackground()顯示錯誤的動畫。

    init()初始化各個Attribute，清空usedIndex(ArrayList<Integer>)將每個字標示為未答對。

    changePicture1,2()隨機找到一個未答對的圖片，更換圖片，重新設定picture1,2的x,y座標。

    changeBackground()根據傳入的參數，開啟一個Thread去不斷更改color以顯示答對或答錯動畫。

    correct()答對時的動作，執行changePicture1,2(),changeBackground()，並將答對的圖片
    設定為已答對，之後不會重複出現。以及執行GameStage的addScore()讓分數增加。最後則是將
    使用者的答案加入toOutput(ArrayList<Word>)，以記錄玩家的答案。

    paintComponent()畫出picture1,2以及依據color設定背景顏色。

    最重要的run()，如同GameStage由檢查stop的while迴圈組成，迴圈中，將picture1,2的y座標
    不斷+1讓他們往下跑，並檢查是否跑到底部了，若跑到底部則執行changePicture1,2()換圖片，
    迴圈中最後檢查GameStage的stop是否為true，若是的話代表右半邊已經判斷贏了，則將自己的stop
    也設成true，下一次迴圈就會跳出。跳出迴圈之後，PrintWriter將使用者的答案toOutput輸出到
    output.txt。


Ball:
    存放球的x,y座標以及球的Image，建構式讀入Image。

BG:
    存放背景的x,y座標以及背景的Image，建構式讀入Image。

Duck:
    存放鴨子的x,y座標,上下漂浮的方向,上下漂浮的中心點以及Image，建構式讀入Image。

Word:
    如同Pair，存放檔名及對應的字。

Reassembler:
    第二部分作業的程式，依據cmd輸入的argument去讀取資料，並使用Scanner去scan，每一行有幾行
    幾列的資料，以及對應的字，將字依據行列放在String[20][20] word中。
    scan完檔案之後，用for迴圈掃描word的1~15列及行，有字就印出字，若為null就印出"---"。

遇到的困難：
    作業說明剛開始看不太懂，後來就決定只採用pdf上部分的作法。
    GUI元件不夠熟悉，常常要上網搜尋才知道要怎麼做。
    Layout使用方式也不太清楚，嘗試了很多次，後來決定不用layout manager才把元件放到對的位置。
    作業第二部分看不懂要我們做什麼，還好問了同學才知道。
