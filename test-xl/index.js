let counter = 0

const counterLimit = Number(process.argv[2]) || 300000

main()

function main() {
  console.log('Counter ', counter)
  counter ++
  if (counter <= counterLimit) {
    setTimeout(main, 1000)
  }
  else {
    console.log('system stoped')
  }
}