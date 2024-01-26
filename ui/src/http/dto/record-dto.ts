export type RecordDto = {
    id: number,
    username: string,
    createdMilli: number,
    productKey: string,
    status: string,
    expandedAllFields: Record<string, string>
}