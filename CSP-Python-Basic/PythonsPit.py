'''
"The Python's Pit" by Ethan S

This is a game that has the player
traversing a dangerous, snake infested cave
with the goal of defeating King Py
and claiming his treasure.

You will have to challenge snakes in number battles
and kill before you're killed in order to survive!

Good luck...

CREDITS:
J. Goldsmith for the clear_output(), image, and audio commands.
Stack Overflow forums for the sys.exit() command.
Jupyter Widgets for the display widgets.
Google Images for the stock snake photos.
Roblox and Tetr.io for the various sound effects. 
'''

# SOUND EFFECTS ARE DISABLED DUE TO POST-GRAD GOOGLE DRIVE ASSET UNRELIABILITY

## Imports ##
!pip install colorama
import colorama
import time
import random
from sys import exit                        # - game over "crash function"
from IPython.display import clear_output    # - clear output for page-by-page gameplay
from IPython.display import HTML            # - images
from google.colab import output             # - audio
from ipywidgets import IntProgress          # - progress bar
from ipywidgets import IntSlider            # - interface for defending


## Variables and Widgets ##
# NOTE: All of these will become global
HP = 100            # Your current health
enemy_HP = 100      # The enemy's health, used in battle
enemy_maxHP = 100   # The enemy's max health, used in battle
turns = 1           # How many turns a battle has been going on for
battleMode = False  # A bool that determines if we are in battle mode

enemyBar = IntProgress(value=100, min=0, max=100, step=1, bar_style='', orientation='horizontal')
defenseSlider1 = IntSlider(value=0, min=0, max=45, step=1, description='', disabled=True, continuous_update=False, orientation='horizontal', readout=False,)
defenseSlider2 = IntSlider(value=0, min=0, max=45, step=1, description='', disabled=True, continuous_update=False, orientation='horizontal', readout=False,)


## Functions ##
#[[Display FNs]]#
def write(waitTime, string1, string2, string3):
    # A handy way to make slow three line descriptions
    time.sleep(waitTime)  
    if string1 != None:
        print(string1)
    time.sleep(waitTime)
    if string2 != None:
        print(string2)
    time.sleep(waitTime)
    if string3 != None:
        print(string3)
    time.sleep(waitTime)

def health_color():
    # Returns the color of your health bar
    if HP >= 50:
        return colorama.Fore.GREEN
    if HP >= 25:
        return colorama.Fore.YELLOW
    if HP >= 0.01:
        return colorama.Fore.RED

def health_bars():
    # Prints health bars at the top of the output; also responsible for game overs
    if HP > 0:
        print(health_color() + "HP | " + str(round(HP, 2)))
    else:
        print(colorama.Fore.RED + "OOF")
        #output.eval_js('new Audio("https://drive.google.com/uc?export=download&id=1R7E059pyT203aGqiKGYmG3COQApNLWyk").play()')
        exit("You perished in the Python's Pit! Too bad... Your death has also caused this entire python program to crash. Try again!")
    if battleMode == True:
        display(enemyBar)
    print(colorama.Style.RESET_ALL + "")

def snake_image(level):
    # Determines which snake image to display during battle
    if level == 1:
        display(HTML('<img src="https://cdn.discordapp.com/attachments/744254592589627392/766332707566649354/snake.png">'))
    if level == 2:
        display(HTML('<img src="https://cdn.discordapp.com/attachments/744254592589627392/766927030989488128/snake2.png">'))
    if level == 3:
        display(HTML('<img src="https://cdn.discordapp.com/attachments/744254592589627392/767218839464312852/snake3.png">'))

def snake_intro(level):
    # Determines what text to display at the start of battles
    if level == 1:
        write(1, "A baby snake appeared!", "[HP: 100 | ATK: 10-15 | DEG: 45° | DEF: x1]", "Only 5 months old, he doesn't pose much of a threat. Ready for battle? \n")
    if level == 2:
        write(1, "Greg the Green Snake jumps in!", "[HP: 200 | ATK: 15-20 | DEG: 60° | DEF: x2]", "Greg was the father of the snake you just eliminated. Get ready, he isn't going to go down as easily! \n")
    if level == 3:
        write(1, "King Py emerges with ferocity!", "[HP: 350 | ATK: 25-30 | DEG: 90° | DEF: x3]" , "King Py is notorious for slaying adventurers via his sneaky error questions. Answer purple problems correctly at all costs! \n")

#[[Number FNs]]#
def player_health_change(number):
    # Makes HP a global variable in order to change it
    global HP
    HP = HP + number

def enemy_health_change(number, setHP):
    # Makes enemy_HP a global variable in order to change it
    global enemy_HP
    global enemy_maxHP

    enemy_HP = enemy_HP + number
    enemyBar.value = enemy_HP

    if setHP != None: # due to a strange glitch new enemies get their health set here
        enemy_HP = setHP
        enemyBar.value = setHP
        enemy_maxHP = setHP
        enemyBar.max = setHP

def operation_roll(level):
    # Decides what type of math problem to give
    roll = random.randint(1,3)
    if level == 1:
        if roll == 1 or roll == 2 or roll == 3:
            return "+"
    if level == 2:
        if roll == 1 or roll == 2:
            return "+"
        elif roll == 3:
            return "-"
    if level == 3:
        if roll == 1:
            return "+"
        elif roll == 2:
            return "-"
        elif roll == 3:
            return "x"

def mutator_roll(level):
    # Decides if a math problem should also be a CRIT or an ERROR
    roll = random.randint(1,8)
    if level == 1:
        return "none"
    if level == 2:
        if roll == 1:
            return "crit"
        else:
            return "none"
    if level == 3:
        if roll == 1:
            return "crit"
        elif roll == 2:
            return "error"
        else:
            return "none"

#[[Battle Mechanic FNs]]#
def math_attack(level):
    # Generates and answers math questions, then deals damage based on player performance
    baseDamage = 20

    for counter in range(1,6):
        if enemy_HP > 0:
            correct = False # is the player correct/incorrect? this bool determines that

            # generate values, larger second value if it's a crit problem
            operation = operation_roll(level)
            mutator = mutator_roll(level)
            value1 = random.randint(0,12)
            value2 = random.randint(0,12)
            if mutator == "crit":
                value2 = random.randint(13,20)

            # problem is presented and player inputs their answer - different print statements for each operation and mutator
            timeStart = time.time()
            if operation == "+":
                solution = value1 + value2
                if mutator == "none":
                    print(str(value1) + " + " + str(value2) + " = ?")
                if mutator == "crit":
                    print(colorama.Fore.LIGHTCYAN_EX + str(value1) + " + " + str(value2) + " = ?")
                if mutator == "error":
                    print(colorama.Fore.MAGENTA + str(value1) + " + " + str(value2) + " = ?")
                answer = input("")
                if answer == str(solution):
                    correct = True

            if operation == "-":
                solution = value1 - value2
                if mutator == "none":
                    print(str(value1) + " - " + str(value2) + " = ?")
                if mutator == "crit":
                    print(colorama.Fore.LIGHTCYAN_EX + str(value1) + " - " + str(value2) + " = ?")
                if mutator == "error":
                    print(colorama.Fore.MAGENTA + str(value1) + " - " + str(value2) + " = ?")
                answer = input("")
                if answer == str(solution):
                    correct = True

            if operation == "x":
                solution = value1 * value2
                if mutator == "none":
                    print(str(value1) + " x " + str(value2) + " = ?")
                if mutator == "crit":
                    print(colorama.Fore.LIGHTCYAN_EX + str(value1) + " x " + str(value2) + " = ?")
                if mutator == "error":
                    print(colorama.Fore.MAGENTA + str(value1) + " x " + str(value2) + " = ?")                
                answer = input("")
                if answer == str(solution):
                    correct = True

            # problem is evaluated, takes into account its mutator
            timeEnd = time.time()
            timeTaken = (timeEnd - timeStart) * level
            damage = round(baseDamage - timeTaken, 0)
            if damage < 0:
                damage = 0

            if correct == True:
                if mutator == "none" or mutator == "error":
                    print(colorama.Fore.GREEN + "Correct! Dealt " + str(damage) + " damage")
                    #output.eval_js('new Audio("https://drive.google.com/uc?export=download&id=1b2vpvg2jIloN7BXFk6xi9FMXOF8dlvvU").play()')
                    print(colorama.Style.RESET_ALL + "")
                    enemy_health_change(damage * -1, None)
                if mutator == "crit":
                    damage = damage * 2
                    print(colorama.Fore.LIGHTCYAN_EX + "CRITICAL HIT! Dealt " + str(damage) + " damage")
                    #output.eval_js('new Audio("https://drive.google.com/uc?export=download&id=1nizdPI8Fy1XxjdxWQP1ZFnu4xnoSYdl_").play()')
                    print(colorama.Style.RESET_ALL + "")
                    enemy_health_change(damage * -1, None)

            if correct == False:
                if mutator == "none" or mutator == "crit":
                    print(colorama.Fore.RED + "Incorrect.")
                    #output.eval_js('new Audio("https://drive.google.com/uc?export=download&id=1wmxt-lRJD2nPkHfuWd2fdnP0hIfLF6zx").play()')
                    print(colorama.Style.RESET_ALL + "")
                if mutator == "error":
                    print(colorama.Fore.MAGENTA + "ERROR... You hurt yourself for " + str(damage) + " damage")
                    #output.eval_js('new Audio("https://drive.google.com/uc?export=download&id=1ic4YzhM1wKL3W0LCULppmLerAG4k19Lh").play()')
                    print(colorama.Style.RESET_ALL + "")
                    player_health_change(damage * -1)

    # finish
    input("[Next] ")
    clear_output()

def number_defense(level, crit):
    # The player will defend by trying to guess the snake's attack direction (random number)
    attackDirection = 0
    resultDamage = 0

    # generating damage value, attack direction, and considering crits
    if level == 1:
        baseDamage = random.randint(10, 15)
        attackDirection = random.randint(0,45)
        info = "[Enter 0 to 45] "
    if level == 2:
        baseDamage = random.randint(15, 20)
        attackDirection = random.randint(0,60)
        defenseSlider1.max = 60
        defenseSlider2.max = 60
        info = "[Enter 0 to 60] "
    if level == 3:
        attackDirection = random.randint(0,90)
        baseDamage = random.randint(25, 30)
        defenseSlider1.max = 90
        defenseSlider2.max = 90
        info = "[Enter 0 to 90] "
    if crit == True:
        baseDamage = baseDamage * 2

    # player inputs their direction
    yourDirection = int(input(info) or "-999")
    defenseSlider1.value = yourDirection
    defenseSlider2.value = attackDirection

    # display the attack and defense using IntSliders
    print("\n \x1B[3mYour Defense \x1B[23m" + "{" + str(yourDirection) + "}")
    if yourDirection != -999:
        display(defenseSlider1)
    else:
        print(colorama.Fore.RED + " <!> NO BLOCK <!> " + colorama.Style.RESET_ALL)
    display(defenseSlider2)
    if crit == False:
        print(" \x1B[3mEnemy's Attack \x1B[23m" + "{" + str(attackDirection) + "} \n")
    elif crit == True:
        print(colorama.Fore.LIGHTCYAN_EX + " \x1B[3mEnemy's Attack (CRIT!) \x1B[23m" + colorama.Style.RESET_ALL + "{" + str(attackDirection) + "} \n")

    # calculate result damage and tell the player how they did
    distance = abs(int(attackDirection) - int(yourDirection))
    if distance == 0:
        resultDamage = round(baseDamage * 0, 2)
        print(colorama.Fore.CYAN + "PERFECT BLOCK! -" + str(resultDamage) + " HP")
    elif distance < 5:
        resultDamage = round(baseDamage * 0.1, 2)
        print(colorama.Fore.BLUE + "Amazing block! -" + str(resultDamage) + " HP")
    elif distance < 15:
        resultDamage = round(baseDamage * 0.25, 2)
        print(colorama.Fore.GREEN + "Good block! -" + str(resultDamage) + " HP")
    elif distance < 30:
        resultDamage = round(baseDamage * 0.5, 2)
        print(colorama.Fore.YELLOW + "Okay block! -" + str(resultDamage) + " HP")
    elif distance < 50:
        resultDamage = round(baseDamage * 0.75, 2)
        print(colorama.Fore.MAGENTA + "Bad block! -" + str(resultDamage) + " HP")
    else:
        resultDamage = round(baseDamage, 2)
        print(colorama.Fore.RED + "Missed block... -" + str(resultDamage) + " HP")

    # change health and finish
    print(colorama.Style.RESET_ALL)
    player_health_change(resultDamage * -1)
    #output.eval_js('new Audio("https://drive.google.com/uc?export=download&id=1-Ht1OHogQQdqF6lnkoetKlnPg-48Ntnh").play()')
    input("[Next] ")
    clear_output()            

def battleLoop(level):
    # This loops the battle mechanics until the enemy dies or the program crashes due to HP = 0
    global battleMode
    battleMode = True
    global turns
    turns = 0

    if level == 1:
        enemy_health_change(0, 100)
    if level == 2:
        enemy_health_change(0, 200)
        enemyBar.value = 200
    if level == 3:
        enemy_health_change(0, 350)
        enemyBar.value = 350

    health_bars()
    snake_image(level)
    snake_intro(level)
    input("[Attack!] ")
    clear_output()

    while enemy_HP > 0:
        turns = turns + 1

        # starting on turn 3 snakes will deal double damage (crits)
        crit = False
        if turns > 2:
            crit = True            

        # attack
        health_bars()
        snake_image(level)
        print("Answer the questions correctly to deal damage! Faster answers deal more damage!")
        math_attack(level)

        # defense
        if enemy_HP > 0: # this check is necessary since enemy_HP becomes 0 in the attack phase
            health_bars()
            snake_image(level)
            print("Enter a number, this is the direction you'll defend! The closer you are, the more damage you block!")
            if turns > 2:
                print(colorama.Fore.LIGHTCYAN_EX + "WARNING: The snake is getting angry! Each attack by the snake is now a Critical Hit!" + colorama.Style.RESET_ALL)
            number_defense(level, crit)
    
    battleMode = False


## Main Function ##
def event_sequence():
    # This is the master function that makes up the whole game!
    clear_output() # required due to colorama's stuff

    health_bars()
    display(HTML('<img src="https://cdn.discordapp.com/attachments/744254592589627392/766330144112771072/WELCOME_1_1.png">'))
    write(1, "Welcome to the Python's Pit!", "Would you like to go through a short tutorial explaining the game's mechanics?", 'Enter "yes" for a tutorial, enter anything else to jump right into the pit. \n')
    option = (input("[Yes or No?] ")).lower()
    clear_output()

    if option == "yes":
        health_bars()
        display(HTML('<img src="https://cdn.discordapp.com/attachments/744254592589627392/766330144112771072/WELCOME_1_1.png">'))
        write(1, "Well, adventurer. You've managed to find the legendary Python's Pit.", "Legend says that only the most intelligent people can survive the Pit and claim its treasure.", "The rules of the Pit are simple. \n")
        write(1, "You will encounter various snakes along your journey.", "These snakes are special, and can only be defeated through the power of numbers.", "Defeat them all and the treasure is yours! \n")
        input("[Next - Jump in!] ")
        clear_output()

    battleLoop(1)
    clear_output()

    player_health_change(25)
    health_bars()
    display(HTML('<img src="https://cdn.discordapp.com/attachments/744254592589627392/766925831875854356/snake1DEAD.png">'))
    #output.eval_js('new Audio("https://drive.google.com/uc?export=download&id=1dS2zQIHaNrFLEXie96MAxy18WRUliOme").play()')
    write(1, "Good job! You've defeated the first snake.", "You gained 25 HP!", "You also gained the ability to deal Critical Hits! Look out for tougher, blue problems. \n")
    input("[Next - Go Deeper] ")
    clear_output()

    health_bars()
    display(HTML('<img src="https://cdn.discordapp.com/attachments/744254592589627392/766926342385041408/Cave1.png">'))
    write(1, "The pit only seems to get darker.", "This can't mean anything good.", "A hissing sound breaks the pit's eerie silence. Get ready! \n")
    input("[Next - Fight!] ")
    clear_output()

    battleLoop(2)
    clear_output()

    player_health_change(50)
    health_bars()
    display(HTML('<img src="https://cdn.discordapp.com/attachments/744254592589627392/766927050757505074/snake2DEAD.png">'))
    #output.eval_js('new Audio("https://drive.google.com/uc?export=download&id=1dS2zQIHaNrFLEXie96MAxy18WRUliOme").play()')
    write(1, "Excellent! You've defeated the second snake.", "You gained another 50 HP!", "You're so close! One battle remains, keep going deeper! \n")
    input("[Next - Go Deeper] ")
    clear_output()

    health_bars()
    display(HTML('<img src="https://cdn.discordapp.com/attachments/744254592589627392/767218800990748672/Cave2.png">'))
    write(1, "This is it. You've reached the deepest depths of the Python's Pit.", "It's time for the final showdown.", "Give it your all! \n")
    input("[Next - Fight!] ")
    clear_output()

    battleLoop(3)
    clear_output()

    health_bars()
    display(HTML('<img src="https://cdn.discordapp.com/attachments/744254592589627392/767218881252032532/snake3DEAD.png">'))
    #output.eval_js('new Audio("https://drive.google.com/uc?export=download&id=1dS2zQIHaNrFLEXie96MAxy18WRUliOme").play()')
    write(1, "CONGRATULATIONS!", "You did it! You eliminated King Py!", "Give yourself a pat on the back for taking this beast down. \n")
    input("[Next - Victory] ")
    clear_output()

    health_bars()
    display(HTML('<img src="https://cdn.discordapp.com/attachments/744254592589627392/767219488759480370/TreasureChestEnd.png">'))
    write(1, "Your journey ends here.", "The legendary treasure of King Py is yours for the taking!", "Enjoy your new life as a victor of the Python's Pit! \n")


event_sequence()