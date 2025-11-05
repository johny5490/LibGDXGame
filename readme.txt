libGDX專案
1.啟動程式:jny.game.lwjgl3.Lwjgl3Launcher


需要一個InputAdapter收集事件到Queue,使用ArrayDeque<>,事件可能要自訂類別


GameApplication.handleInput():把事件Queue取出來處理


InputMultiplexer:可以增加多個InputProcessor,其可以按順序分派事件給各個加入的InputProcessor,
				 

怎麼做到整個切換畫面?  

開始的選單畫面 -> 遊戲開始畫面
Stage不能作為一個完整的畫面,它只能是一個畫面中的一塊
實際做法要清掉畫面重繪,要思考怎麼有個架構可以決定畫面,
畫面又伴隨著輸入事件的處理



Stage:也是一種InputProcessor,可以加入各種Actor,各種UI元件都是Actor,
	  UI的事件或者直接在其事件觸發時處理,不用流到事件Queue

PauseMenuStage:遊戲暫停選單


目前需要兩種事件的處理
ESC:暫停遊戲並開啟遊戲選單
遊戲中滑鼠右鍵:主角往點擊目的前進

-------------
畫面類別命名Scene
讓每個畫面有自己的事件處理,先收集事件,可自定義只處理自己畫面需要的事件




