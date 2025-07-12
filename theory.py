



import random 


# Returns false if given 3x3 block contains num 
# Ensure the number is not used in the box 


def unUsedInBox(grid , rowStart , colStart, num):
    for i in range(3):
        for j in range(3):
            if grid[rowStart+i][colStart+j]==num: 
                return False 
    return True 

# Fill a 3x3 matrix 
# Assign valid random numbers to the 3x3 subgrid 

def fillBox(grid, row, col):
    for i in range(3):
        for j in range(3):
            while True:

                num = random.randint(1,9)
                if unUsedInBox(grid, row , col, num):
                    break 
            grid[row+i][col+j]= num 


# Check if it's safe to put num in row i 
# Ensure num is not already used in the row

def unUsedInRow(grid, i , num):
    return num not in grid[i]


# Same just for columns now 

def unUsedInCol(grid, j, num):
    for i in range(9):
        if grid[i][j]==num:
            return False
    return True 

# Check if it's safe to put num in the cell i, j 

def CheckIfSafe(grid ,i ,j ,num):
    return (unUsedInRow(grid, i, num) and 
            unUsedInCol(grid , j, num) and 
            unUsedInBox(grid, i - i%3, j-j%3, num))

# fill the diagonal 3x3 matrices 
# The diagonal blocks are filled to simplify the process

def fillDiagonal(grid):
    for i in range(0, 9, 3):

        # Fill each 3x3 subgrid diagonally 
        fillBox(grid , i , i)

# Fill remaining blocks in the grid 
# Recursively fill the remaining cells with valid numbers 


def fillRemaining(grid , i ,j):

    #If we've reached the end of the grid 
    if i == 9:
        return True 
    
    # Move to next row when current row is finished 
    if j==9:
        return fillRemaining(grid , i+1, 0)
    

    # Skip if cell already filled 
    if grid[i][j] != 0:
        return fillRemaining(grid , i , j+1)
    

    # Try numbers 1-9 in current cell
    for num in range(1, 10):
        if CheckIfSafe(grid ,i ,j, num):
            grid[i][j] = num 
            if fillRemaining(grid ,i , j+1):
                return True 
            grid[i][j] = 0 
    return False 



# Remove k digits randomly from the grid 
# This will create a Sudoku puzzle by removing digits 

def removeKDigits(grid , k):
    while k>0:
        # Pick a random cell
        cellId = random.randint(0,80)

        i, j = cellId // 9, cellId % 9

        # Remove the digit if the cell is not already empty 
        if grid[i][j] != 0:
            # Empty cell 
            grid[i][j] = 0
            # Decrease the count of digits to remove 
            k -= 1

# Generate a Sudoku grid with K empty cells 
def sudokuGenerator(k):


    # Initialize an empty 9x9 grid 
    grid = [[0] * 9 for _ in range(9)]

    # Fill the diagonal 3x3 matrices 

    fillDiagonal(grid)

    # Fill the remaining blocks in the grid 
    fillRemaining(grid , 0, 0)

    # Remove K digits randomly to create the puzzle 
    removeKDigits(grid , k)

    return grid 


if __name__ == "__main__":
    random.seed()
    k = 20
    sudoku = sudokuGenerator(k)

    print("\n\n\u25A0 GENERATED SUDOKU PUZZLE \u25A0\n")
    print("+-------+-------+-------+")
    for i, row in enumerate(sudoku):
        print("|", end=" ")
        for j, num in enumerate(row):
            cell = str(num) if num != 0 else "."
            print(cell, end=" ")
            if (j + 1) % 3 == 0:
                print("|", end=" ")
        print()
        if (i + 1) % 3 == 0:
            print("+-------+-------+-------+")













