import axios from "axios"

const mockedAxios = jest.createMockFromModule('axios') as jest.Mocked<typeof axios>

// this is the key to fix the axios.create() undefined error!
mockedAxios.create.mockReturnThis()

export default mockedAxios